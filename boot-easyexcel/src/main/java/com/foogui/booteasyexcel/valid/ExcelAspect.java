package com.foogui.booteasyexcel.valid;

import com.foogui.common.exception.BizException;
import com.foogui.common.utils.LocalCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Aspect
@Slf4j
@Component
public class ExcelAspect {


    /**
     * 切入点为@ExcelValid注解标注的方法
     */
    @Pointcut("@annotation(com.foogui.booteasyexcel.valid.ExcelValid)")
    public void annotationCutPoint() {
    }

    @Around("annotationCutPoint() && @annotation(excelValid)")
    public Object around(ProceedingJoinPoint joinPoint, ExcelValid excelValid) throws ClassNotFoundException {
        Object excelDTO = joinPoint.getArgs()[0];

        String className = excelDTO.getClass().getName();

        Class<?> clz = Class.forName(className);

        Map<String, Object> cacheMap = LocalCacheUtil.getCacheMap(className);
        // 缓存Field，避免反射影响性能
        if (Objects.isNull(cacheMap)) {
            Field[] declaredFields = clz.getDeclaredFields();
            HashMap<String, Object> injectMap = new HashMap<>();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                injectMap.put(field.getName(), field);
            }
            LocalCacheUtil.setCacheMap(className,injectMap);
            cacheMap=injectMap;
        }
        cacheMap.values().forEach(field->{
            if (field instanceof Field) {
                Field temp = (Field) field;

                Arrays.stream(temp.getAnnotations()).forEach(annotation->{
                    if (annotation instanceof ExcelNotNull){
                        handleExcelNotNull(excelDTO,temp,(ExcelNotNull)annotation);
                    } else if (annotation instanceof ExcelDigits) {
                        handleExcelDigits(excelDTO,temp,(ExcelDigits)annotation);
                    }
                });
            }
        });

        Object result;

        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new BizException(e.getMessage());
        }

        return result;


    }


    /**
     * 处理excel数字类型校验
     *
     * @param object     对象
     * @param field      字段
     * @param annotation 注释
     */
    private void handleExcelDigits(Object object, Field field, ExcelDigits annotation) {
        try {
            Object value = field.get(object);
            String message = annotation.message();
            if (!(object instanceof Number)) {
                throw new IllegalArgumentException(message);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 处理excel非空注解判断逻辑
     *
     * @param field      场
     * @param annotation 注释
     */
    private void handleExcelNotNull(Object object,Field field, ExcelNotNull annotation) {
        try {
            Object value = field.get(object);
            String message = annotation.message();
            if (null == value || value.equals("")) {
                throw new IllegalArgumentException(message);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
