//
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.baomidou.mybatisplus.generator.FastAutoGenerator;
//import com.baomidou.mybatisplus.generator.config.OutputFile;
//import com.baomidou.mybatisplus.generator.config.rules.DateType;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class MybatisPlusGeneratorTest {
//
//
//    public static void main(String[] args) {
//        new MybatisPlusGenerator().generate();
//    }
//
//    /**
//     * 生成容器编排文件.
//     */
//    private void generate() {
//        String jdbcUrl = "jdbc:mysql://wk-server1:3306/order?serverTimezone=GMT%2B8";
//        String dataBaseUserName = "root";
//        String dataBasePwd = "james";
//        String packageName = "com.wk.order";
//        String author = "wk";
//        boolean fileOverride = true;
//        FastAutoGenerator fastAutoGenerator = FastAutoGenerator.create(jdbcUrl, dataBaseUserName, dataBasePwd);
//        Map<OutputFile, String> pathInfo = new HashMap<>();
////        sourceDirectory:D:\workspace\research\business\order\order-dao\src\main\java
//        String sourceDirectory = "D:\\workspace\\research\\business\\order\\order-dao\\src\\main\\java";
//
//        String packageDir = packageName.replace(".", "\\");
//
//
//        String controllerDir = sourceDirectory.replace("dao", "controller") + "\\" + packageDir + "\\controller\\base";
//        String serviceDir = sourceDirectory.replace("dao", "service") + "\\" + packageDir + "\\service\\base";
//        String entityDir = sourceDirectory.replace("dao", "entity") + "\\" + packageDir + "\\entity\\base";
//        String daoDir = sourceDirectory + "\\" + packageDir + "\\mapper\\base";
//        String xmlDir = sourceDirectory.substring(0, sourceDirectory.lastIndexOf("\\")) + "\\resources\\" + packageDir + "\\mapper\\base";
//
//
//        pathInfo.put(OutputFile.mapperXml, xmlDir);
//        pathInfo.put(OutputFile.mapper, daoDir);
//        pathInfo.put(OutputFile.entity, entityDir);
//        pathInfo.put(OutputFile.controller, controllerDir);
//        pathInfo.put(OutputFile.service, serviceDir);
//        pathInfo.put(OutputFile.serviceImpl, serviceDir + "\\impl");
//
//
//        // 全局配置
//
//        fastAutoGenerator
//                .globalConfig(builder -> {
//                    builder.author(author)
////                        .fileOverride() // 覆盖已生成文件
//                            .enableSwagger();
//                    if (fileOverride) {
//                        builder.fileOverride();
//                    }
//                    builder.dateType(DateType.TIME_PACK);
//                    builder.disableOpenDir(); //不要打开目录了
//                    builder.enableKotlin();
//                })
//                // 包配置
//                .packageConfig(
//                        builder -> builder.parent(packageName)
//                                .moduleName("") // 设置父包模块名
//                                .entity("entity.base")
//                                .service("service.base")
//                                .serviceImpl("service.base.impl")
//                                .mapper("mapper.base")
//                                .xml("mapper.xml")
//                                .controller("controller.base")
//                                // 设置生成路径
//                                .pathInfo(pathInfo)
//                )
//
//                //controller策略
//                .strategyConfig(builder ->
//                                builder.controllerBuilder()
////                .superClass(BaseController.class)
//                                        .enableHyphenStyle()
//                                        .enableRestStyle()
//                                        .formatFileName("%sController")
//                                        .build()
//                )
//
//                //service策略
//                .strategyConfig(builder ->
//                                builder.serviceBuilder()
//                                        .serviceBuilder()
////                                                .superServiceClass(BaseService.class)
////                                                .superServiceImplClass(BaseServiceImpl.class)
//                                        .formatServiceFileName("%sService")
//                                        .formatServiceImplFileName("%sServiceImp")
//                                        .build()
//                )
//
//                //dao策略
//                .strategyConfig(builder ->
//                                builder.mapperBuilder()
//                                        .superClass(BaseMapper.class)
//                                        .enableMapperAnnotation()
//                                        .enableBaseResultMap()
//                                        .enableBaseColumnList()
////                              .cache(MyMapperCache.class)
//                                        .formatMapperFileName("%sMapper")
//                                        .formatXmlFileName("%sMapper")
//                                        .build()
//                )
//
//                // entity策略
//                .strategyConfig(
//                        builder -> builder.entityBuilder()
////                            .superClass(BaseEntity.class)
////                            .disableSerialVersionUID()
//                                .enableChainModel()
//                                .enableLombok()
//                                .enableRemoveIsPrefix()
//                                .enableTableFieldAnnotation()
//                                .enableActiveRecord()
//                                .versionColumnName("version")
//                                .versionPropertyName("version")
//                                .logicDeleteColumnName("delFlag")
//                                .logicDeletePropertyName("delFlag")
//
////                                .naming(NamingStrategy.underline_to_camel)
////                                .columnNaming(NamingStrategy.underline_to_camel)
////                            .addSuperEntityColumns("id", "create_by", "create_time", "update_by", "update_time")
////                            .addTableFills(new Column("create_time", FieldFill.INSERT))
////                            .addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE))
//                                .idType(IdType.AUTO)
//                                .formatFileName("%s")
//                                .build()
//                )
//                .strategyConfig(builder ->
//                                builder.mapperBuilder()
//                                        .superClass(BaseMapper.class)
//                                        .enableMapperAnnotation()
//                                        .enableBaseResultMap()
//                                        .enableBaseColumnList()
////                .cache(MyMapperCache.class)
//                                        .formatMapperFileName("%sMapper")
//                                        .formatXmlFileName("%sMapper")
//                                        .build()
//                )
//
//                .strategyConfig((scanner, builder) ->
//                                builder.enableCapitalMode()
//                                        .enableSkipView()
////                            .disableSqlFilter()
//                                        .addInclude(scanner.apply("请输入表名:"))
////                            .likeTable(new LikeTable("order_"))
//                )
//                //.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
//                .execute();
//    }
//
//}
