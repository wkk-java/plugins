package com.wk.plugin;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @author: vince
 * 初始构建所有表(不覆盖).
 */
@Mojo(name = "generateAllTable", defaultPhase = LifecyclePhase.COMPILE)
public class MybatisPlusGeneratorAllTable extends BaseMojo {


    /**
     * 执行.
     */
    public void execute() {
        if (!"jar".equals(project.getPackaging())) {
            getLog().info("非jar打包方式，跳过构建...");
            return;
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
                builder
                        .enableCapitalMode()
                        .enableSkipView()
        )
                .execute();
    }

}
