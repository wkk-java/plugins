package com.wk.plugin;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: vince
 * create at: 2020/4/19 23:03
 * @description: 生成docker compose编排插件
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.COMPILE)
public class MybatisPlusGenerator extends AbstractMojo {
    /**
     * maven项目对象.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;


    /**
     * 执行.
     *
     * @throws MojoExecutionException 执行异常
     */
    public void execute() throws MojoExecutionException {
        if (!"jar".equals(project.getPackaging())) {
            getLog().info("非jar打包方式，跳过构建...");
            return;
        }
        super.getLog().info(project.getName() + ":" + project.getVersion() + "开始构建...");
        generate();
        super.getLog().info(project.getName() + ":" + project.getVersion() + "构建结束...");
    }

    /**
     * 生成容器编排文件.
     *
     * @throws MojoExecutionException 执行异常
     */
    private void generate() throws MojoExecutionException {
        StrategyConfig beanConfig = new StrategyConfig.Builder()
                .entityBuilder()
//                .superClass(BaseEntity.class)
                .disableSerialVersionUID()
                .enableChainModel()
                .enableLombok()
                .enableRemoveIsPrefix()
                .enableTableFieldAnnotation()
                .enableActiveRecord()
                .versionColumnName("version")
                .versionPropertyName("version")
                .logicDeleteColumnName("deleted")
                .logicDeletePropertyName("deleteFlag")
                .naming(NamingStrategy.no_change)
                .columnNaming(NamingStrategy.underline_to_camel)
                .addSuperEntityColumns("id", "created_by", "created_time", "updated_by", "updated_time")
                .addIgnoreColumns("age")
                .addTableFills(new Column("create_time", FieldFill.INSERT))
                .addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE))
                .idType(IdType.AUTO)
                .formatFileName("%sEntity")
                .build();


        StrategyConfig controllerConfig = new StrategyConfig.Builder()
                .controllerBuilder()
//                .superClass(BaseController.class)
                .enableHyphenStyle()
                .enableRestStyle()
                .formatFileName("%sController")
                .build();


        StrategyConfig serviceConfig = new StrategyConfig.Builder()
                .serviceBuilder()
//                .superServiceClass(BaseService.class)
//                .superServiceImplClass(BaseServiceImpl.class)
                .formatServiceFileName("%sService")
                .formatServiceImplFileName("%sServiceImp")
                .build();

        StrategyConfig mapperConfig = new StrategyConfig.Builder()
                .mapperBuilder()
                .superClass(BaseMapper.class)
                .enableMapperAnnotation()
                .enableBaseResultMap()
                .enableBaseColumnList()
//                .cache(MyMapperCache.class)
                .formatMapperFileName("%sMapper")
                .formatXmlFileName("%sXml")
                .build();


        FastAutoGenerator fastAutoGenerator = FastAutoGenerator.create("jdbc:mysql://wk-server1:3306/order?serverTimezone=GMT%2B8", "root", "");
        Map<OutputFile, String> pathInfo = new HashMap<>();
        pathInfo.put(OutputFile.mapperXml, "D:\\workspace\\research\\business\\order\\order-dao\\src\\main\\resources\\com\\wk\\order\\mapper\\base");
        pathInfo.put(OutputFile.controller, "D:\\workspace\\research\\business\\order\\order-controller\\src\\main\\java\\com\\wk\\order\\controller");
        pathInfo.put(OutputFile.mapper, "D:\\workspace\\research\\business\\order\\order-dao\\src\\main\\java\\com\\wk\\order\\mapper\\base");
        pathInfo.put(OutputFile.entity, "D:\\workspace\\research\\business\\order\\order-entity\\src\\main\\java\\com\\wk\\order\\entity\\base");
        pathInfo.put(OutputFile.service, "D:\\workspace\\research\\business\\order\\order-service\\src\\main\\java\\com\\wk\\order\\service\\base");
        pathInfo.put(OutputFile.serviceImpl, "D:\\workspace\\research\\business\\order\\order-service\\src\\main\\java\\com\\wk\\order\\service\\base\\impl");

//        Collections.singletonMap(OutputFile.mapperXml, "D://")
        fastAutoGenerator.globalConfig(builder -> {
            builder.author("wk") // 设置作者
                    .enableSwagger() // 开启 swagger 模式
//                            .fileOverride() // 覆盖已生成文件
//                            .outputDir("D://"); // 指定输出目录
            ;
        })
                .packageConfig(builder -> {
                    builder.parent("com.wk.order") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("mapper")
                            .xml("mapper.xml")
                            .controller("controller")
                            .pathInfo(pathInfo); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder()
//                            .superClass(BaseEntity.class)
                            .disableSerialVersionUID()
                            .enableChainModel()
                            .enableLombok()
                            .enableRemoveIsPrefix()
                            .enableTableFieldAnnotation()
                            .enableActiveRecord()
                            .versionColumnName("version")
                            .versionPropertyName("version")
                            .logicDeleteColumnName("deleted")
                            .logicDeletePropertyName("deleteFlag")
                            .naming(NamingStrategy.underline_to_camel)
                            .columnNaming(NamingStrategy.underline_to_camel)
                            .addSuperEntityColumns("id", "create_by", "create_time", "update_by", "update_time")
                            .addTableFills(new Column("create_time", FieldFill.INSERT))
                            .addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE))
                            .idType(IdType.AUTO)
                            .formatFileName("%sEntity")
                            .build();
                })
                .strategyConfig(builder -> {
                    builder.mapperBuilder()
                            .superClass(BaseMapper.class)
                            .enableMapperAnnotation()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
//                .cache(MyMapperCache.class)
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sXml")
                            .build();
                })

                .strategyConfig(builder -> {
                    builder.enableCapitalMode()
                            .enableSkipView()
                            .disableSqlFilter()
                            .likeTable(new LikeTable("order%s"));
                })
                //.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
