package com.foogui.boot.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AopLock {

    /**
     * 锁的名字
     * @return {@link String}
     */
    String lockName() default "";

    /**
     * 锁的有效期
     * @return long
     */
    long duration() default 5L;

    /**
     * 时间单位
     * @return {@link TimeUnit}
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 未抢到锁的提示信息
     * @return {@link String}
     */
    String description() default "操作过于频繁，请重试";
}
