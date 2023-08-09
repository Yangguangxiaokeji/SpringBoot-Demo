package com.foogui.bootasync.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 配置信息封装成Bean
 *
 * @author Foogui
 * @date 2023/08/09
 */
@Data
@ConfigurationProperties(prefix = "async.executor.thread")
@Component
public class ExecutorConfigProperty {
    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
    private String namePrefix;
    private int keepAliveSeconds;
    private Set<String> excludeSet;
}
