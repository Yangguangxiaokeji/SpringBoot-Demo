package com.foogui.boot.aop.aspect;

import com.foogui.boot.aop.RedisService;
import com.foogui.boot.aop.annotation.AopLock;
import com.foogui.boot.aop.utils.MyAnnotationUtils;
import com.foogui.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Component
@Aspect
public class LockAspect {

    @Autowired
    private RedisService redisService;


    @Pointcut("@annotation(com.foogui.boot.aop.annotation.AopLock)")
    public void lockAspect() {

    }

    @Around("lockAspect() && @annotation(aopLock)")
    public Object around(ProceedingJoinPoint joinPoint, AopLock aopLock) {
        String lockName = aopLock.lockName();

        if (StringUtils.isNotBlank(lockName)) {
            lockName = aopLock.lockName();
        } else if (StringUtils.isNotBlank(aopLock.expression())) {
            String methodName = joinPoint.getSignature().getName();
            Class<?> targetClass = joinPoint.getTarget().getClass();
            for (Method method : targetClass.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    // todo:spel去执行方法存在问题
                    lockName = MyAnnotationUtils.resolveExpressionValue(method);
                }
            }
        } else {
            // 如果未定义lockName和SPEL,就将所有参数拼接
            lockName = Arrays.stream(joinPoint.getArgs())
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
        }
        boolean isLock = redisService.tryLock(lockName, aopLock.duration(), aopLock.timeUnit());
        Object obj = null;
        try {
            if (isLock) {
                obj = joinPoint.proceed();
            }else {
                throw new BizException(aopLock.description());
            }

        } catch (Throwable e) {
            throw new BizException(e.getMessage());
        } finally {
            redisService.unlock(lockName);
        }
        return obj;
    }
}
