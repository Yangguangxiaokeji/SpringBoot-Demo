package com.foogui.boot.aop.aspect;

import com.foogui.boot.aop.RedisService;
import com.foogui.boot.aop.annotation.AopLock;
import com.foogui.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
