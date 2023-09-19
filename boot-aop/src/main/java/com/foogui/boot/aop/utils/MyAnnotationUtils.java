package com.foogui.boot.aop.utils;

import com.foogui.boot.aop.annotation.AopLock;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class MyAnnotationUtils {
    public static String resolveExpressionValue(Method method) {
        AopLock annotation = AnnotationUtils.findAnnotation(method, AopLock.class);
        if (annotation != null) {
            String expression = StringUtils.hasText(annotation.expression()) ? annotation.expression() : annotation.lockName();
            if (StringUtils.hasText(expression)) {
                ExpressionParser parser = new SpelExpressionParser();
                Expression spelExpression = parser.parseExpression(expression);
                return spelExpression.getValue(String.class);
            }
        }
        return null;
    }
}
