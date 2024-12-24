package cn.lemnt.code;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.apache.ibatis.annotations.Mapper;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/** * @author wangxx
 * @version 1.0.0
 * @date 2024/11/5 15:02
 */
public class AutoCodeGenerator {

    /**
     * 数据源
     */
    private static final String DATASOURCE_URL = "jdbc:mysql://localhost:3306/layban_pro";

    /**
     * 用户名
     */
    private static final String USERNAME = "root";

    /**
     * 密码
     */
    private static final String PASSWORD = "root";

    private static final String MODULE_NAME = "";

    private static final String OUTPUT_PATH = System.getProperty("user.dir") + "/tmp";

    /**
     * 父包名
     */
    private static final String PARENT_PATH = "com.layban";

    /**
     * 作者
     */
    private static final String AUTHOR = "wangxx";

    /**
     * 表名
     */
    private static final List<String> TABLES = new ArrayList<>(
            Arrays.asList("system_dict_data"));
    /**
     * 表名
     */
    private static final List<String> TABLE_PREFIX = new ArrayList<>(
            Arrays.asList("system_"));

    public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get(OUTPUT_PATH + "/src/main/java"));
            Files.createDirectories(Paths.get(OUTPUT_PATH + "/src/main/resources/mapper"));
        } catch (Exception e) {
            return; // Exit if the directory creation fails
        }
        FastAutoGenerator.create(DATASOURCE_URL, USERNAME, PASSWORD)
                .globalConfig(builder -> builder
                        .author(AUTHOR)
                        .outputDir(OUTPUT_PATH + "/src/main/java")
                )
                .packageConfig(builder -> builder
                        .parent(PARENT_PATH)
                        .moduleName(MODULE_NAME)
                        .entity("model.dataobject")
                        .service("service")
                        .serviceImpl("service.impl")
                        .mapper("mapper")
                        .xml("mapper.xml")
                        .controller("controller")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, OUTPUT_PATH + "/src/main/resources/mapper"))
                )
                .strategyConfig(builder -> builder
                        .addInclude(TABLES)
                        .addTablePrefix(TABLE_PREFIX)
                        .entityBuilder()
                        .enableLombok()
                        .superClass("com.layban.common.model.BaseModel")
                        .addSuperEntityColumns("creator", "create_time", "update_time", "updater")
                        .versionColumnName("version")
                        .logicDeleteColumnName("deleted")
                        .javaTemplate("/templates/DO.java")
                        .disable()
                        .controllerBuilder()
                        .enableRestStyle()
                        .enableHyphenStyle()
                        .superClass("com.layban.common.controller.BaseController")
                        .template("/templates/adapter/controller.java")
                        .serviceBuilder()
                        .superServiceClass("com.layban.common.service.BaseService")
                        .superServiceImplClass("com.layban.common.service.BaseServiceImpl")
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")
                        .serviceTemplate("/templates/service.java")
                        .serviceImplTemplate("/templates/serviceImpl.java")
                        .mapperBuilder()
                        .mapperAnnotation(Mapper.class)
                        .enableBaseResultMap()
                        .enableBaseColumnList()
                        .mapperTemplate("/templates/mapper.java")
                        .mapperXmlTemplate("/templates/mapper.xml")
                )
                .injectionConfig(consumer -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("add", "model.vo.add");
                    map.put("update", "model.vo.update");
                    map.put("vo", "model.vo");
                    map.put("qry", "model.vo.qry");
                    map.put("convert", "model.convert");
                    consumer.customMap(map);
                    List<CustomFile> customFiles = new ArrayList<>();
                    customFiles.add(new CustomFile.Builder()
                            .packageName("model.vo.add")
                            .fileName("AddVO.java")
                            .templatePath("/templates/adapter/vo/AddVO.java.vm")
                            .enableFileOverride()
                            .build());
                    customFiles.add(new CustomFile.Builder()
                            .packageName("model.vo.update")
                            .fileName("UpdateVO.java")
                            .templatePath("/templates/adapter/vo/UpdateVO.java.vm")
                            .enableFileOverride()
                            .build());
                    customFiles.add(new CustomFile.Builder()
                            .packageName("model.vo")
                            .fileName("VO.java")
                            .templatePath("/templates/adapter/vo/VO.java.vm")
                            .enableFileOverride()
                            .build());
                    customFiles.add(new CustomFile.Builder()
                            .packageName("model.vo.qry")
                            .fileName("PageVO.java")
                            .templatePath("/templates/adapter/vo/PageVO.java.vm")
                            .enableFileOverride()
                            .build());
                    customFiles.add(new CustomFile.Builder()
                            .packageName("model.convert")
                            .fileName("Convert.java")
                            .templatePath("/templates/convert/Convert.java.vm")
                            .enableFileOverride()
                            .build());
                    consumer.customFile(customFiles);
                }).templateEngine(new VelocityTemplateEngine()).execute();
    }
}
