package com.wk.plugin;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author: vince
 * 构建包含的表.
 */
@Mojo(name = "generateIncludeTables", defaultPhase = LifecyclePhase.COMPILE)
public class MybatisPlusGeneratorIncludeTables extends BaseMojo {

    @Parameter(required = true, readonly = true)
    protected String includeTables;
    /**
     * 执行.
     */
    public void execute() throws MojoExecutionException {
        if (!"jar".equals(project.getPackaging())) {
            getLog().info("非jar打包方式，跳过构建...");
            return;
        }
        if (StringUtils.isBlank(includeTables)) {
            throw new MojoExecutionException("参数[includeTables]为空!");
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

        fastAutoGenerator.strategyConfig((scanner, builder) ->
                        builder.enableCapitalMode()
                                .enableSkipView()
//                            .disableSqlFilter()
//                             .addInclude(scanner.apply("请输入表名:"))
                                .addInclude(includeTables.toLowerCase().split(","))
        )
                .execute();
    }

}
