package com.person.norma.basicservice;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.person.norma.basiccommon.gererator.common.GeneratorContext;
import com.person.norma.basiccommon.gererator.config.DBConfig;
import com.person.norma.basiccommon.gererator.entry.TableInfo;
import com.person.norma.basiccommon.gererator.service.GeneratorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BasicserviceApplication.class})
public class GeneratorTests {

    @Autowired
    private DBConfig config;

    @Autowired
    private GeneratorService service;

    @Test
    public void contextLoads() {
        ApplicationContext context = GeneratorContext.getApplicationContext();
        config = context.getBean(DBConfig.class);
        service = context.getBean(GeneratorService.class);
        doGenerator("sys_user", false);
    }

    /**
     * @ Description   :  ไปฃ็ ็ๆ
     * @ Author        :  norma
     * @ CreateDate    :  2020/12/23 11:41
     */
    private void doGenerator(String tableName, boolean flag) {
        TableInfo tableInfo = service.queryAllColumns(tableName.toUpperCase(), config);
        String model = tableName.replace("_", "").toLowerCase();
        String comments = tableInfo.getComments();
        AutoGenerator generator = new AutoGenerator();
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(config.getOutPath());
        globalConfig.setFileOverride(true);
        globalConfig.setActiveRecord(true);
        // xml ไบ็บง็ผๅญ
        globalConfig.setEnableCache(true);
        // xml resultMap
        globalConfig.setBaseResultMap(true);
        // xml columnList
        globalConfig.setBaseColumnList(false);
        globalConfig.setDateType(DateType.ONLY_DATE);
        globalConfig.setAuthor(config.getAuthor());

        // ่ชๅฎไนๆไปถๅฝๅ๏ผๆณจๆ%sไผ่ชๅจๅกซๅ่กจๅฎไฝๅฑๆง
        globalConfig.setEntityName("%sEntity");
        globalConfig.setMapperName("%sDao");
        globalConfig.setXmlName("%sDao");
        globalConfig.setServiceName("%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setControllerName("%sController");
        generator.setGlobalConfig(globalConfig);

        // ๆฐๆฎๅบ้็ฝฎ
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(config.getUrl().contains("mysql") ? DbType.MYSQL : DbType.ORACLE);
        dataSourceConfig.setDriverName(config.getDriver());
        dataSourceConfig.setUsername(config.getUsername());
        dataSourceConfig.setPassword(config.getPassword());
        dataSourceConfig.setUrl(config.getUrl());
        generator.setDataSource(dataSourceConfig);

        // ็ญ็ฅ้็ฝฎ
        StrategyConfig strategyConfig = new StrategyConfig();
        if (flag
                && tableName.contains("_")
                && tableName.lastIndexOf("_") != tableName.length() - 1) {
            String prefix = tableName.substring(0, tableName.indexOf("_") + 1);
            // ๆญคๅคๅฏไปฅไฟฎๆนไธบๆจ็่กจๅ็ผ
            strategyConfig.setTablePrefix(prefix);
            // ่กจๅ็ๆ็ญ็ฅ
            NamingStrategy.removePrefix(tableName, prefix);
        } else {
            // ้ป่ฎค่กจๅ็ๆ็ญ็ฅ
            strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        }
        // ้่ฆ็ๆ็่กจ
        strategyConfig.setInclude(tableName);

        // ๆ้ค็ๆ็่กจ
        // strategyConfig.setExclude(tableName);

        // ๅญๆฎตๅ็ๆ็ญ็ฅ
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);

        // ่ชๅฎไนๅฎไฝๅฌๅฑๅญๆฎต
        // strategyConfig.setSuperEntityColumns("createTime", "updateTime");

        // ่ชๅฎไน mapper ็ถ็ฑป
        // strategyConfig.setSuperMapperClass("com.person.norma");

        // ่ชๅฎไน service ็ถ็ฑป
        // strategyConfig.setSuperServiceClass("com.person.norma");

        // ่ชๅฎไน serviceImpl ็ถ็ฑป
        // strategyConfig.setSuperServiceImplClass("com.person.norma");

        // ่ชๅฎไน controller ็ถ็ฑป
        // strategyConfig.setSuperControllerClass("com.person.norma");

        // ใๅฎไฝใๆฏๅฆ็ๆๅญๆฎตๅธธ้๏ผ้ป่ฎค false๏ผ
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);

        // ใๅฎไฝใๆฏๅฆไธบๆๅปบ่ๆจกๅ๏ผ้ป่ฎค false๏ผ
        // public User setName(String name) {this.name = name; return this;}
        strategyConfig.setEntityBuilderModel(true);
        generator.setStrategy(strategyConfig);

        // ๅ้็ฝฎ
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(config.getPackagePath());
        packageConfig.setModuleName(model);
        packageConfig.setService("service");
        packageConfig.setServiceImpl("service.impl");
        packageConfig.setMapper("dao");
        packageConfig.setXml("resource");
        packageConfig.setEntity("entity");
        generator.setPackageInfo(packageConfig);

        // ๆณจๅฅ่ชๅฎไน้็ฝฎ๏ผๅฏไปฅๅจ VM ไธญไฝฟ็จ cfg.abc ่ฎพ็ฝฎ็ๅผ
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("swaggerControllerNote", comments);
                map.put("entityLombokModel", true);
                map.put("restControllerStyle", true);
                this.setMap(map);
            }
        };
        generator.setCfg(cfg);

        // ่ชๅฎไนๆจกๆฟ้็ฝฎ๏ผๅฏไปฅ copy ๆบ็  mybatis-plus/src/main/resources/template ไธ้ขๅๅฎนไฟฎๆน๏ผ
        // ๆพ็ฝฎ่ชๅทฑ้กน็ฎ็ src/main/resources/template ็ฎๅฝไธ, ้ป่ฎคๅ็งฐไธไธๅฏไปฅไธ้็ฝฎ๏ผไนๅฏไปฅ่ชๅฎไนๆจกๆฟๅ็งฐ
        TemplateConfig tc = new TemplateConfig();
        tc.setController("/templates/gen-template/controller.java.vm");
        tc.setEntity("/templates/gen-template/entity.java.vm");
        tc.setMapper("/templates/gen-template/dao.java.vm");
        tc.setXml("/templates/gen-template/dao.xml.vm");
        tc.setService("/templates/gen-template/service.java.vm");
        tc.setServiceImpl("/templates/gen-template/serviceImpl.java.vm");
        generator.setTemplate(tc);
        // ๆง่ก็ๆ
        generator.execute();
    }

}
