package com.foogui.bootevent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Configuration
public class ListenerConfig {

    //把线程池赋值进去
    @Bean
    public SimpleApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = new SimpleApplicationEventMulticaster();
        simpleApplicationEventMulticaster.setTaskExecutor(taskExecutor());
        return simpleApplicationEventMulticaster;
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 500, 10, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1000));
        return executor;
    }
}
