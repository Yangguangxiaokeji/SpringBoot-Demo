package com.foogui.boot.mybatisplus;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * MP代码生成器
 *
 * @author Foogui
 * @date 2023/07/30
 */
public class CodeGenerator {
    private static final String JAVA = "D:/Git/Github/SpringBoot-Demo/boot-mybatisplus/src/main/java";

    private static final String PARENT ="com.foogui.boot.mybatisplus";

    private static final String RESOURCES = "D:/Git/Github/SpringBoot-Demo/boot-mybatisplus/src/main/resources/mapper";

    private static final Map<OutputFile, String> pathInfo= new HashMap<>();

    static {
        pathInfo.put(OutputFile.mapperXml, RESOURCES);

    }

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3308/mybatisplusdemo?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", "root", "root")
                .globalConfig(builder -> {
                    builder.author("FooGui") // 设置作者
                            // .enableSwagger() // 开启 swagger 模式
                            //.fileOverride() // 覆盖已生成文件

                            .outputDir(JAVA); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(PARENT) // 设置父包名
                            .moduleName("test") // 设置父包模块名
                            .pathInfo(pathInfo);// 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("sys_role")    // 设置需要生成的表名
                    // .addInclude("sys_role")   // 添加多个表
                    // .addInclude("sys_user_role")   // 添加多个表
                    // .addTablePrefix("t_")       // 设置过滤表前缀
                    ;
                }).templateEngine(new FreemarkerTemplateEngine())// 使用Freemarker
                .execute();
    }
}
