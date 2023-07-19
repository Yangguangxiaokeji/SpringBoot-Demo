package com.foogui.boot.aop.annotation;


import java.lang.annotation.*;

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
