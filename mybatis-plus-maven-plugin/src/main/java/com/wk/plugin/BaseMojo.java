package com.wk.plugin;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseMojo extends AbstractMojo {
    /**
     * maven项目对象.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject project;

    @Parameter(required = true, readonly = true)
    protected String jdbcUrl;
    @Parameter(defaultValue = "root", required = true, readonly = true)
    protected String dataBaseUserName;
    @Parameter(defaultValue = "root", required = true, readonly = true)
    protected String dataBasePwd;
    @Parameter(required = true, readonly = true)
    protected String author;
    @Parameter(defaultValue = "com.wk", required = true, readonly = true)
    protected String packageName;
//    @Parameter(defaultValue = "false", required = false, readonly = true)
//    protected boolean fileOverride;


    private Map getPathInfo() {
        Map<OutputFile, String> pathInfo = new HashMap<>();
//        sourceDirectory:D:\workspace\research\business\order\order-dao\src\main\java
//        directory:D:\workspace\research\business\order\order-dao\target
        String sourceDirectory = project.getBuild().getSourceDirectory();

        String packageDir = packageName.replace(".", "\\");

        String controllerDir = sourceDirectory.replace("dao", "controller") + "\\" + packageDir + "\\controller";
        String serviceDir = sourceDirectory.replace("dao", "service") + "\\" + packageDir + "\\service";
        String entityDir = sourceDirectory.replace("dao", "entity") + "\\" + packageDir + "\\entity";
        String daoDir = sourceDirectory + "\\" + packageDir + "\\mapper";
        String xmlDir = sourceDirectory.substring(0, sourceDirectory.lastIndexOf("\\")) + "\\resources\\" + packageDir + "\\mapper";

        super.getLog().info("构建目录controllerDir]:" + controllerDir);
        super.getLog().info("构建目录[serviceDir]:" + serviceDir);
        super.getLog().info("构建目录[entityDir]:" + entityDir);
        super.getLog().info("构建目录[daoDir]:" + daoDir);
        super.getLog().info("构建目录[xmlDir]:" + xmlDir);

        pathInfo.put(OutputFile.mapperXml, xmlDir);
        pathInfo.put(OutputFile.mapper, daoDir);
        pathInfo.put(OutputFile.entity, entityDir);
        pathInfo.put(OutputFile.controller, controllerDir);
        pathInfo.put(OutputFile.service, serviceDir);
        pathInfo.put(OutputFile.serviceImpl, serviceDir + "\\impl");

        return pathInfo;
    }

    protected FastAutoGenerator buildGenerator() {
        FastAutoGenerator fastAutoGenerator = FastAutoGenerator.create(jdbcUrl, dataBaseUserName, dataBasePwd);
        fastAutoGenerator
                .globalConfig(builder -> {
                    builder.author(author)
                            .enableSwagger();
                    //不允许覆盖
//                        builder.fileOverride();
                    builder.dateType(DateType.TIME_PACK);
                    builder.disableOpenDir(); //不要打开目录了
                })
                // 包配置
                .packageConfig(
                        builder -> builder.parent(packageName)
                                .moduleName("") // 设置父包模块名
                                .entity("entity")
                                .service("service")
                                .serviceImpl("service.impl")
                                .mapper("mapper")
                                .xml("mapper")
                                .controller("controller")
                                // 设置生成路径
                                .pathInfo(getPathInfo())
                )

                //controller策略
                .strategyConfig(builder ->
                                builder.controllerBuilder()
//                .superClass(BaseController.class)
                                        .enableHyphenStyle()
                                        .enableRestStyle()
                                        .formatFileName("%sController")
                                        .build()
                )

                //service策略
                .strategyConfig(builder ->
                                builder.serviceBuilder()
                                        .serviceBuilder()
//                                                .superServiceClass(BaseService.class)
//                                                .superServiceImplClass(BaseServiceImpl.class)
                                        .formatServiceFileName("%sService")
                                        .formatServiceImplFileName("%sServiceImp")
                                        .build()
                )

                //dao策略
                .strategyConfig(builder ->
                                builder.mapperBuilder()
                                        .superClass(BaseMapper.class)
                                        .enableMapperAnnotation()
                                        .enableBaseResultMap()
                                        .enableBaseColumnList()
//                              .cache(MyMapperCache.class)
                                        .formatMapperFileName("%sMapper")
                                        .formatXmlFileName("%sMapper")
                                        .build()
                )

                // entity策略
                .strategyConfig(
                        builder -> builder.entityBuilder()
//                            .superClass(BaseEntity.class)
//                            .disableSerialVersionUID()
                                .enableChainModel()
                                .enableLombok()
                                .enableRemoveIsPrefix()
                                .enableTableFieldAnnotation()
                                .enableActiveRecord()
                                .versionColumnName("version")
                                .versionPropertyName("version")
                                //逻辑删除
                                .logicDeleteColumnName("del_flag")
                                .logicDeletePropertyName("delFlag")
                                .naming(NamingStrategy.underline_to_camel)
                                .columnNaming(NamingStrategy.underline_to_camel)
//                            .addSuperEntityColumns("id", "create_by", "create_time", "update_by", "update_time")
                                .addTableFills(new Column("crt_time", FieldFill.INSERT))
                                .addTableFills(new Column("create_time", FieldFill.INSERT))
                                .addTableFills(new Column("upd_time", FieldFill.INSERT_UPDATE))
                                .addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE))
                                .idType(IdType.ASSIGN_ID)
                                .formatFileName("%s")
                                .build()
                )
                .strategyConfig(builder ->
                                builder.mapperBuilder()
                                        .superClass(BaseMapper.class)
                                        .enableMapperAnnotation()
                                        .enableBaseResultMap()
                                        .enableBaseColumnList()
//                .cache(MyMapperCache.class)
                                        .formatMapperFileName("%sMapper")
                                        .formatXmlFileName("%sMapper")
                                        .build()
                );
        return fastAutoGenerator;
    }
}
