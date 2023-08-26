package com.foogui.bootevent.template.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

/**
 * 自定义的线程池-Spring
 *
 * @author Foogui
 * @date 2023/04/08
 */
@Configuration
public class ProjectExecutorPool {

    @Autowired
    private ProjectExecutorsConfig projectExecutorsConfig;

    @Bean
    public ThreadPoolTaskExecutor eventExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(projectExecutorsConfig.getCorePoolSize());
        executor.setMaxPoolSize(projectExecutorsConfig.getMaxPoolSize());
        executor.setKeepAliveSeconds(projectExecutorsConfig.getKeepAliveSeconds());
        executor.setQueueCapacity(projectExecutorsConfig.getQueueCapacity());
        executor.setThreadNamePrefix(projectExecutorsConfig.getThreadNamePrefix());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
