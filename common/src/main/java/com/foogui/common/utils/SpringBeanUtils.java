package com.foogui.common.utils;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * 从IOC中获取bean的工具类
 * 避免使用类再去实现扩展点；需要交给IOC才能生效
 *
 * @author Foogui
 * @date 2023/05/19
 */

public class SpringBeanUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtils.applicationContext = applicationContext;
    }

    /**
     * 根据Class类型从容器中获取Bean
     *
     * @param clazz clazz
     * @return {@link T}
     */
    public static <T> T getBean(Class<T> clazz) {

        return applicationContext != null ? applicationContext.getBean(clazz) : null;
    }

    /**
     * 根据Class类型和名字从容器中获取Bean
     *
     * @param beanName bean名字
     * @param clazz    clazz
     * @return {@link T}
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {

        return applicationContext != null ? applicationContext.getBean(beanName, clazz) : null;
    }

    /**
     * 根据Bean名字从容器中获取Bean
     *
     * @param beanName bean名字
     * @return {@link Object}
     */
    public static Object getBean(String beanName) {
        return applicationContext != null ? applicationContext.getBean(beanName) : null;
    }

    /**
     * 获取Class下所有的Bean实例
     *
     * @param clazz clazz
     * @return {@link Map}<{@link String}, {@link T}>
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return applicationContext != null ? applicationContext.getBeansOfType(clazz) : null;
    }

    /**
     * 得到目标对象target的代理对象
     * 需要在配置类上开启代理暴露，使用注解 @EnableAspectJAutoProxy(exposeProxy = true)
     *
     * @param target 目标对象
     * @return {@link T}
     */
    public static <T> T getAopProxy(T target) {
        return (T) AopContext.currentProxy();
    }
}