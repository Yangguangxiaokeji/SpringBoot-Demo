# boot-async

> 此 demo 展示在SpringBoot中如何使用异步进行编程

## pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.foogui</groupId>
        <artifactId>SpringBoot-Demo</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <artifactId>boot-async</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <name>boot-async</name>
    <description>boot-async</description>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.foogui</groupId>
            <artifactId>common</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>

```

## application.properties

```properties
# 异步线程配置
# 配置核心线程数
async.executor.thread.core_pool_size = 20
# 配置最大线程数
async.executor.thread.max_pool_size = 10000
# 配置队列大小
async.executor.thread.queue_capacity = 500
# 配置线程池中的线程的名称前缀
async.executor.thread.name.prefix = foogui-async-
# 核心线程存在空闲时间
async.executor.thread.keep_alive_seconds = 200
```

## 启动类

```java
@SpringBootApplication
public class BootAsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootAsyncApplication.class, args);
    }

}
```

## 核心代码

### AsyncConfiguration

```java
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
    @Value("${async.executor.thread.name.prefix}")
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
```

### Task

```java
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
```

## 测试结果

```bash
开始做任务三
开始做任务一
foogui-async-1完成任务一，耗时：268毫秒
foogui-async-2完成任务三，耗时：716毫秒
任务全部完成，总耗时：729毫秒
任务1总耗时：268毫秒
任务2总耗时：716毫秒
```

## 参考学习

待添加
