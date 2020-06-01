package com.wk.plugin;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: vince
 * create at: 2020/4/19 23:03
 * @description: 生成docker compose编排插件
 */
@Mojo(name="generate",defaultPhase = LifecyclePhase.COMPILE)
public class DockerComposeMojo extends AbstractMojo {
    /**
     * maven项目对象.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;
    /**
     * 应用归属组.
     */
    @Parameter
    private String applicationGroup = "application";
    /**
     * docker私服地址.
     */
    @Parameter(required = true)
    private String dockerNexusServer = "wk-aliyun";

    /**
     * docker私服端口.
     */
    @Parameter(required = true)
    private String dockerNexusServerPort = "18082";

    /**
     * 环境名称.
     */
    private String ymlProfile = null;
    /**
     * 应用端口.
     */
    private Integer serverProt = 8080;


    /**
     * 执行.
     * @throws MojoExecutionException 执行异常
     */
    public void execute() throws MojoExecutionException {
        if (!"jar".equals(project.getPackaging())) {
            getLog().info("非jar打包方式，跳过构建...");
            return;
        }
        super.getLog().info(project.getName() + ":" + project.getVersion() + "开始构建...");
        //resolveApplicationYaml();
        generate();
        super.getLog().info(project.getName() + ":" + project.getVersion() + "构建结束...");
    }

    /**
     * 生成容器编排文件.
     * @throws MojoExecutionException 执行异常
     */
    private void generate() throws MojoExecutionException{
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
        configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/"));

        String templateName ="application";
        String serverProtOut = serverProt == null ? "6000" : serverProt.toString();
        if ("framework".equalsIgnoreCase(applicationGroup)) {
            serverProtOut = serverProt == null ? "3000" : serverProt.toString();
//            templateName = "";
        }

        OutputStreamWriter writer = null;
        try {
            Template template = configuration.getTemplate(templateName + ".yml.ftl");
            String filePath = project.getBuild().getDirectory() + "/" + project.getName() + ".yml";
            getLog().info("编排文件:" + filePath);
            File file = new File(project.getBuild().getDirectory());
            if (!file.exists()) {
                file.mkdir();
            }
            Map<String, List<String>> activeProfileMap = project.getInjectedProfileIds();
            if (activeProfileMap == null || activeProfileMap.isEmpty()) {
                throw new MojoExecutionException("profile is empty!");
            }
            String profileIdKey = project.getParentArtifact().getGroupId() + ":" + project.getParentArtifact().getArtifactId() + ":" + project.getParentArtifact().getVersion();
//            getLog().info("activeProfileMap:" + activeProfileMap.toString());
            List<String> activeProfileIdList = activeProfileMap.get(profileIdKey);
            String profile = String.join(",", activeProfileIdList);

            writer = new FileWriter(new File(filePath));
            Map<String, Object> context = new HashMap<String, Object>();
            context.put("project", project);
            context.put("projectName", project.getName());
            context.put("projectVersion", project.getVersion());
            context.put("profile", profile);
            context.put("dockerNexusServerPrefix", dockerNexusServer + ":" + dockerNexusServerPort + "/");
            context.put("outsidePort", serverProtOut);
            context.put("insidePort", serverProt.toString());
            template.process(context, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * 解析应用的yaml配置.
     * @throws MojoExecutionException 执行失败
     */
    private void resolveApplicationYaml() throws MojoExecutionException {
        List<Resource> resources = project.getResources();
        Resource resource = resources.get(0);
        String yamlFilePathString = resource.getDirectory() + "/bootstrap.yaml";
        //getLog().info("springcloud启动配置:" + yamlFilePathString);
        File yamlFile = new File(yamlFilePathString);
        if (!yamlFile.exists()) {
            getLog().error("bootstrap.yml文件不存在,请确认!");
            throw new MojoExecutionException("bootstrap.yml文件不存在,请确认!");
        }
        try {
            Yaml yaml = new Yaml();
            Iterable<Object> iterators = yaml.loadAll(new FileInputStream(yamlFile));
            Iterator<Object> iterator = iterators.iterator();
            Map<String, Integer> serverPortMap = new HashMap<String, Integer>();
            while (iterator.hasNext()) {
                Object nextObj = iterator.next();
                getLog().info("nextObj:" + nextObj);
                LinkedHashMap nextMap = (LinkedHashMap) nextObj;
                getLog().info("nextMap:" + nextMap.toString());

                if (nextMap.toString().contains("{server={port=")) {
                    serverProt = (Integer) ((LinkedHashMap) nextMap.get("server")).get("port");
                }
                if (nextMap.toString().contains("{spring={profiles={active")) {
                    ymlProfile = (String)((LinkedHashMap)((LinkedHashMap)nextMap.get("spring")).get("profiles")).get("active");
                } else if(nextMap.toString().contains("{spring={profiles=") && nextMap.toString().contains("{server={port=")) {
                    String profileKey = (String) ((LinkedHashMap) nextMap.get("spring")).get("profiles");
                    Integer serverPort = (Integer) ((LinkedHashMap) nextMap.get("server")).get("port");
                    serverPortMap.put(profileKey, serverPort);
                }
            }
            getLog().info(yamlFilePathString + ",activeProfile is:" + ymlProfile);
            serverProt = ymlProfile != null ? serverPortMap.get(ymlProfile) : null;
        } catch (Exception ex) {
            ex.printStackTrace();
            getLog().error(ex);
        }
    }

    public static void main(String[] args) throws Exception {
        String filePath = "/Users/vince/Documents/workspace/research/framework/eureka-server/src/main/resources/bootstrap.yaml";
    }

}
