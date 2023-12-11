package com.foogui.bootasync;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> orgFuture = CompletableFuture.supplyAsync(
                ()->{
                    System.out.println("原始CompletableFuture方法任务");
                    return "捡田螺的小男孩";
                }
        );

        CompletableFuture<Integer> thenApplyFuture = orgFuture.thenApply((a) -> {
            if ("捡田螺的小男孩".equals(a)) {
                return 666;
            }

            return 111;
        });

        System.out.println(thenApplyFuture.get());
    }
}

