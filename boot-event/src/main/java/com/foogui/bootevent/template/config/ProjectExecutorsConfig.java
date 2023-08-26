package com.foogui.bootevent.template.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("executor")//读取配置文件的线程池属性
public class ProjectExecutorsConfig {

    private int corePoolSize;

    private int maxPoolSize;

    private int queueCapacity;

    private int keepAliveSeconds;

    private String threadNamePrefix;


}
