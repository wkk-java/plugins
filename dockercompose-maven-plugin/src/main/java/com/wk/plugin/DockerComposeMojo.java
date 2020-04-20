package com.wk.plugin;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
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
     * 模板名称.
     */
    @Parameter(defaultValue = "application")
    private String templateName;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;
    /**
     * 环境名称.
     */
    private String profile = "dev";

    /**
     * 应用端口.
     */
    private Integer serverProt = 8080;

    /**
     * 执行.
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        super.getLog().info(project.getName() + ":" + project.getVersion() + "开始构建...");
        resolveApplicationYaml();
        generate();
        super.getLog().info(project.getName() + ":" + project.getVersion() + "构建结束...");
    }

    /**
     * 生成容器编排文件.
     */
    private void generate() {
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
        configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/"));

        OutputStreamWriter writer = null;
        try {
            Template template = configuration.getTemplate(templateName + ".yml.ftl");
            String filePath = project.getBuild().getDirectory() + "/" + project.getName() + ".yml";
            getLog().info("编排文件:" + filePath);
            File file = new File(project.getBuild().getDirectory());
            if (!file.exists()) {
                file.mkdir();
            }
            writer = new FileWriter(new File(filePath));
            Map<String, Object> context = new HashMap<String, Object>();
            context.put("project", project);
            context.put("projectName", project.getName());
            context.put("projectVersion", project.getVersion());
            context.put("profile", profile);
            context.put("outsidePort", "2" + serverProt.toString());
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
    public void resolveApplicationYaml() throws MojoExecutionException {
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
                LinkedHashMap objNext = (LinkedHashMap) iterator.next();
                if (objNext.toString().contains("{spring={profiles={active")) {
                    profile = (String)((LinkedHashMap)((LinkedHashMap)objNext.get("spring")).get("profiles")).get("active");
                } else {
                    String profileKey = (String) ((LinkedHashMap) objNext.get("spring")).get("profiles");
                    Integer serverPort = (Integer) ((LinkedHashMap) objNext.get("server")).get("port");
                    serverPortMap.put(profileKey, serverPort);
                }
            }
            getLog().info("profile is:" + profile);
            serverProt = serverPortMap.get(profile);
        } catch (Exception ex) {
            getLog().error(ex);
        }
    }

    public static void main(String[] args) throws Exception {
        String filePath = "/Users/vince/Documents/workspace/research/framework/eureka-server/src/main/resources/bootstrap.yaml";
    }

}
