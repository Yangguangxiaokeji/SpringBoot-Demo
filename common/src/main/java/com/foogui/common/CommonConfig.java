package com.foogui.common;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，负责扫面Bean并注入IOC
 *
 * @author Foogui
 * @date 2023/08/10
 */
@Configuration
@ComponentScan("com.foogui.common")
// 配合@WebServlet、@WebFilter和@WebListener使用，专门扫描他们
@ServletComponentScan("com.foogui.common.filter")
public class CommonConfig {

}
