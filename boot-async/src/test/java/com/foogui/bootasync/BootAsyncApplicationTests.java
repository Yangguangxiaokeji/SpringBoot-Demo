package com.foogui.bootasync;

import com.foogui.bootasync.domain.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
class BootAsyncApplicationTests {

    @Autowired
    private Task task;

    @Autowired
    @Qualifier("taskExecutor1")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    public void test() throws Exception {
        long start = System.currentTimeMillis();

        CompletableFuture<Long> future1 = task.doTaskOne();
        CompletableFuture<Long> future2 = task.doTaskTwo();
        // 阻塞主线程直到future1和future2执行完，才继续执行
        CompletableFuture.allOf(future1, future2).join();

        long end = System.currentTimeMillis();

        System.out.println("任务全部完成，总耗时：" + (end - start) + "毫秒");
        System.out.println("任务1总耗时：" + future1.get() + "毫秒");
        System.out.println("任务2总耗时：" + future2.get() + "毫秒");
    }

    @Test
    public void testCompletableFuture() {
        // supplyAsync的使用
        CompletableFuture<String> supplyFuture = CompletableFuture.supplyAsync(() -> "supplyAsync执行CompletableFuture任务，支持返回值", threadPoolTaskExecutor);
        System.out.println(supplyFuture.join());
        // runAsync的使用
        CompletableFuture<Void> runFuture = CompletableFuture.runAsync(() -> System.out.println("runAsync执行CompletableFuture任务，没有返回值"), threadPoolTaskExecutor);
        // runAsync的future没有返回值，输出null
        System.out.println(runFuture.join());
        // thenRun:定义先后顺序
        CompletableFuture<Void> thenRunFuture = supplyFuture.thenRun(() -> System.out.println("前后两个任务没有参数传递，第二个任务也没有返回值"));
        System.out.println(thenRunFuture.join());
        // thenAccept：定义先后顺序，入参传递
        CompletableFuture<Void> thenAcceptFuture = supplyFuture.thenAccept((res) -> System.out.println(res));
        System.out.println(thenAcceptFuture.join());
        // thenApply：定义先后顺序，入参传递,有返回值
        CompletableFuture<String> thenApplyFuture = supplyFuture.thenApply((res) -> "foogui:" + res);
        System.out.println(thenApplyFuture.join());
        // exceptionally:捕捉异常
        CompletableFuture<String> problemFuture = CompletableFuture.supplyAsync(() -> {
                    System.out.println("当前线程名称：" + Thread.currentThread().getName());
                    throw new RuntimeException("foogui is cute");
                }
        );
        CompletableFuture<String> exceptionalFuture = problemFuture.exceptionally((e) -> {
            return "你的程序出现了异常" + e.getMessage();
        });
        System.out.println(exceptionalFuture.join());
        // whenComplete:异步回调，自身不能return值，join或get获取的是触发Future的值
        HashMap<String, String> data = new HashMap<>();
        CompletableFuture<HashMap<String, String>> whenCompleteFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程名称：" + Thread.currentThread().getName());
            // 模拟查询的数据
            data.put("1", "1");
            return data;
        });
        CompletableFuture<HashMap<String, String>> whenFuture = whenCompleteFuture.whenComplete((res, e) -> {
            System.out.println("当前线程名称：" + Thread.currentThread().getName());
            // 模拟修改数据
            data.put("1", "foogui");
        });
        // 这里返回的是旧值
        System.out.println(whenFuture.join().get("1"));

        // handle:异步回调，有返回值，join或get是新值
        // 代码省略，参考whenComplete即可

        // AllOf：所有任务都执行完成后，才执行 allOf返回的CompletableFuture
        // AnyOf: 任意一个任务执行完，就执行anyOf返回的CompletableFuture
    }


}
