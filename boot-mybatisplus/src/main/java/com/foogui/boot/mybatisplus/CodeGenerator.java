package com.foogui.boot.mybatisplus;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * MP代码生成器
 *
 * @author Foogui
 * @date 2023/07/30
 */
public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3308/mybatisplusdemo?characterEncoding=utf8&useSSL=false&serverTimezone=UTC", "root", "root")
                .globalConfig(builder -> {
                    builder.author("Foogui") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            //.fileOverride() // 覆盖已生成文件
                   
                            .outputDir("D:/Git-Mine/java/mybatisPlusDemo/demo/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.foogui") // 设置父包名
                            .moduleName("other") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:/Git-Mine/java/mybatisPlusDemo/demo/src/main/resources/mapper"));// 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("sys_user")    // 设置需要生成的表名
                            // .addInclude("sys_role")   // 添加多个表
                            // .addInclude("sys_user_role")   // 添加多个表
                            .addTablePrefix("t_");  // 设置过滤表前缀
                }).templateEngine(new FreemarkerTemplateEngine())// 使用Freemarker
                .execute();
    }
}
