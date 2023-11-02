package com.mashibing.serviceorder.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 自动生成代码类
 */
public class MysqlGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/service-order?characterEncoding=utf-8&serverTimezone=GMT%2B8","root","root@123")
                .globalConfig(builder -> {
                    builder.author("hanjp").fileOverride().outputDir("/Users/hanjianpeng/Idea_Workspace/online-taxi-public/service-order/src/main/java");
                }).packageConfig(builder -> {
                    builder.parent("com.mashibing.serviceorder").pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                            "/Users/hanjianpeng/Idea_Workspace/online-taxi-public/service-order/src/main/java/com/mashibing/serviceorder/mapper"));
                }).strategyConfig(builder -> {
                    builder.addInclude("order_info");
                }).templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
