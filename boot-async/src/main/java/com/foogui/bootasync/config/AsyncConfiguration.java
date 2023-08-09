package com.foogui.bootasync.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义线程池
 *
 * @author Foogui
 * @date 2023/03/29
 */
@Configuration
@Slf4j
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {

    // 冒号可以设置个默认值
    @Value("${async.executor.thread.core_pool_size:5}")
    private int corePoolSize;
    @Value("${async.executor.thread.max_pool_size}")
    private int maxPoolSize;
    @Value("${async.executor.thread.queue_capacity}")
    private int queueCapacity;
    @Value("${async.executor.thread.name_prefix}")
    private String namePrefix;
    @Value("${async.executor.thread.keep_alive_seconds}")
    private int keepAliveSeconds;

    @Bean(name = "taskExecutor1")
    public ThreadPoolTaskExecutor executor() {
        // ThreadPoolTaskExecutor ：最常使用，推荐。其实质是对java.util.concurrent.ThreadPoolExecutor的包装
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        taskExecutor.setCorePoolSize(corePoolSize);
        // 线程池维护线程的最大数量,只有在缓冲队列满了之后才会申请超过核心线程数的线程
        taskExecutor.setMaxPoolSize(maxPoolSize);
        // 缓存队列
        taskExecutor.setQueueCapacity(queueCapacity);
        // 当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        // 异步方法内部线程名称
        taskExecutor.setThreadNamePrefix(namePrefix);
        /**
         * 当线程池的任务缓存队列已满并且线程池中的线程数目达到maximumPoolSize，如果还有任务到来就会采取任务拒绝策略
         * 通常有以下四种策略：
         * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
         * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
         * ThreadPoolExecutor.CallerRunsPolicy：重试添加当前的任务，自动重复调用 execute() 方法，直到成功
         */
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        taskExecutor.initialize();

        return taskExecutor;
    }

    /**
     * 设置并指定默认线程池,当@Async不指定线程池时默认使用该线程池
     */
    @Override
    public Executor getAsyncExecutor() {
        return executor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) ->
                log.error("线程池执行任务发送未知错误,执行方法：{}", method.getName(), ex);
    }
}
