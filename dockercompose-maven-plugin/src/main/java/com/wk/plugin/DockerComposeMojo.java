package com.wk.plugin;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: vince
 * create at: 2020/4/19 23:03
 * @description: 生成docker compose编排插件
 */
@Mojo(name="generate",defaultPhase = LifecyclePhase.COMPILE)
public class DockerComposeMojo extends AbstractMojo {
    /**
     * 应用名称.
     */
    @Parameter(defaultValue = "application")
    private String templateName;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;
    /**
     * 环境名称.
     */
    private String profile = "dev";

    public void execute() throws MojoExecutionException, MojoFailureException {
        super.getLog().info(project.getName() + ":" + project.getVersion() + "开始构建...");
        generate();
        super.getLog().info(project.getName() + ":" + project.getVersion() + "构建结束...");
    }

    private void generate() {
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/"));

        OutputStreamWriter writer = null;
        try {
            Template template = configuration.getTemplate(templateName + ".yml.ftl");
            String filePath = project.getBuild().getDirectory() + "/" + project.getName() + ".yaml";
            getLog().info(filePath);
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
            context.put("outsidePort", "1234");
            context.put("insidePort", "2341");
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


}
