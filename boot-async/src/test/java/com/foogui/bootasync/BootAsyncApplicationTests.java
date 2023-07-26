package com.foogui.bootasync;

import com.foogui.bootasync.domain.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;

@SpringBootTest
class BootAsyncApplicationTests {

    @Autowired
    private Task task;

    @Test
    public void test() throws Exception {
        long start = System.currentTimeMillis();

        CompletableFuture<Long> future1 = task.doTaskOne();
        CompletableFuture<Long> future2 = task.doTaskTwo();
        // 阻塞主线程直到future1和future2执行完，才继续执行
        CompletableFuture.allOf(future1,future2).join();

        long end = System.currentTimeMillis();

        System.out.println("任务全部完成，总耗时：" + (end - start) + "毫秒");
        System.out.println("任务1总耗时：" + future1.get() + "毫秒");
        System.out.println("任务2总耗时：" + future2.get() + "毫秒");
    }



}
