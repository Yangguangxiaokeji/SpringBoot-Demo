package com.foogui.boot.aop.aspect;


import com.foogui.boot.aop.annotation.MyAOP;
import com.foogui.common.exception.BizException;
import com.foogui.common.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


@Aspect
@Slf4j
@Component
@Order(1) //order越小越是最先执行，但更重要的是最先执行的最后结束
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
