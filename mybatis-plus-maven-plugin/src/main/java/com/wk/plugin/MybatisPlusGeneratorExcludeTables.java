package com.wk.plugin;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author: vince
 * 排除某些表进行构建(不覆盖).
 */
@Mojo(name = "generateExcludeTables", defaultPhase = LifecyclePhase.COMPILE)
public class MybatisPlusGeneratorExcludeTables extends BaseMojo {

    @Parameter(required = true, readonly = true)
    protected String excludeTables;

    /**
     * 执行.
     */
    public void execute() throws MojoExecutionException {
        if (!"jar".equals(project.getPackaging())) {
            getLog().info("非jar打包方式，跳过构建...");
            return;
        }
        if (StringUtils.isBlank(excludeTables)) {
            throw new MojoExecutionException("参数[excludeTableNames]为空");
        }
        super.getLog().info(project.getName() + ":" + project.getVersion() + "开始构建...");
        generate();
        super.getLog().info(project.getName() + ":" + project.getVersion() + "构建结束!");
    }

    /**
     * 生成.
     */
    private void generate() {
        FastAutoGenerator fastAutoGenerator = buildGenerator();

        fastAutoGenerator.strategyConfig(builder ->
                builder
                        .enableCapitalMode()
                        .enableSkipView()
                        .addExclude(excludeTables.toLowerCase().split(","))
        )
                .execute();
    }

}
