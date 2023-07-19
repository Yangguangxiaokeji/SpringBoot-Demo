# boot-aop

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

	<artifactId>boot-aop</artifactId>
	<version>0.0.1-SNAPSHOT</version>

	<name>boot-aop</name>

	<description>boot-aop</description>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<!-- aop依赖 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-aop</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
		</dependency>
	</dependencies>



</project>

```

## application.yml

```yaml

```

## 启动类

```java
@SpringBootApplication
@EnableAspectJAutoProxy
public class BootAopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootAopApplication.class, args);
	}

}
```

## 核心代码

### AopAspect

```java
@Aspect
@Slf4j
@Component
public class AopAspect {


    /**
     * 切入点为com.foogui.boot.aop包下所有controller包下的结尾为Controller类中的所有的方法
     */
    @Pointcut("execution(* com.foogui.boot.aop.controller..*.*Controller.*(..))")
    public void methodCutPoint() {
    }

    /**
     * 切入点为@MyAOP注解标注的方法
     */
    @Pointcut("@annotation(com.foogui.boot.aop.annotation.MyAOP)")
    public void annotationCutPoint() {
    }

    @Before("methodCutPoint()")
    public void before(JoinPoint joinPoint) {
        HttpServletRequest request = ServletUtils.getRequest();
        // 在*Controller类的所有方法执行之前执行此方法
        System.out.println("请求来源： =》" + request.getRemoteAddr());
        System.out.println("请求URL：" + request.getRequestURL().toString());
        System.out.println("请求方式：" + request.getMethod());
        System.out.println("响应方法：" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        System.out.println("请求参数：" + Arrays.toString(joinPoint.getArgs()));
    }

    @Around("annotationCutPoint() && @annotation(myAOP)")
    public Object around(ProceedingJoinPoint point, MyAOP myAOP) {

        String className = point.getTarget().getClass().getName();
        log.info("执行的类名：{}", className);
        String methodName = point.getSignature().getName();
        log.info("执行的方法名：{}", methodName);
        String value = myAOP.value();
        log.info("获取注解的value值：{}", value);

        Object result;

        Long startTime = System.currentTimeMillis();
        try {
            result = point.proceed();
        } catch (Throwable e) {
            throw new BizException(e.getMessage());
        } finally {
            Long endTime = System.currentTimeMillis();
            log.info("响应时间为{}", endTime - startTime);
        }

        return result;
    }
}
```

### MyAOP

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAOP {

    /**
     * 描述
     *
     * @return {@link String}
     */
    String value() default "";

}
```

### SysLogController

```java
@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @PostMapping("/test")
    @MyAOP("foogui")
    public AjaxResult testAop(@RequestBody DTO dto){
        return AjaxResult.success("testAop测试成功");
    }

}
```



## 测试结果

```bash
2023-07-19 14:18:47.330  INFO 2040 --- [nio-8080-exec-2] com.foogui.boot.aop.aspect.AopAspect     : 执行的类名：com.foogui.boot.aop.controller.SysLogController
2023-07-19 14:18:47.336  INFO 2040 --- [nio-8080-exec-2] com.foogui.boot.aop.aspect.AopAspect     : 执行的方法名：testAop
2023-07-19 14:18:47.337  INFO 2040 --- [nio-8080-exec-2] com.foogui.boot.aop.aspect.AopAspect     : 获取注解的value值：foogui
请求来源： =》0:0:0:0:0:0:0:1
请求URL：http://localhost:8080/sys/log/test
请求方式：POST
响应方法：com.foogui.boot.aop.controller.SysLogController.testAop
请求参数：[DTO(value=content)]
2023-07-19 14:18:53.972  INFO 2040 --- [nio-8080-exec-2] com.foogui.boot.aop.aspect.AopAspect     : 响应时间为6635
```

## 参考学习

待添加
