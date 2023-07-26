package com.foogui.bootasync.domain;

import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Component
public class Task {

    public static Random random =new Random();

    // 使用taskExecutor1线程池执行
    @Async("taskExecutor1")
    @SneakyThrows
    public CompletableFuture<Long> doTaskOne()  {
        if (false){
            throw new Exception("抛异常");
        }

        System.out.println("开始做任务一");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(5000));
        long end = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName()+"完成任务一，耗时：" + (end - start) + "毫秒");
        return CompletableFuture.completedFuture(end-start);
    }

    // 使用默认线程池
    @Async
    public CompletableFuture<Long> doTaskTwo() throws Exception {
        System.out.println("开始做任务三");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(5000));
        long end = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName()+"完成任务三，耗时：" + (end - start) + "毫秒");
        return CompletableFuture.completedFuture(end-start);
    }

}
