package com.mashibing.servicedriveruser.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 自动生成代码类
 */
public class MysqlGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/service-driver-user?characterEncoding=utf-8&serverTimezone=GMT%2B8","root","root@123")
                .globalConfig(builder -> {
                    builder.author("hanjp").fileOverride().outputDir("/Users/hanjianpeng/Idea_Workspace/online-taxi-public/service-driver-user/src/main/java");
                }).packageConfig(builder -> {
                    builder.parent("com.mashibing.servicedriveruser").pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                            "/Users/hanjianpeng/Idea_Workspace/online-taxi-public/service-driver-user/src/main/java/com/mashibing/servicedriveruser/mapper"));
                }).strategyConfig(builder -> {
                    builder.addInclude("driver_car_binding_relationship");
                }).templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
