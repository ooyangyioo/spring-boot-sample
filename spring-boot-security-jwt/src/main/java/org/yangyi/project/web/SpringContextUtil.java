package org.yangyi.project.web;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 获取应用上下文
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据名称和类型获取Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 根据类型获取Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 根据名称获取Bean
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 获取容器中指定某类型、或实现某接口、或继承某父类所有的 Bean
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return applicationContext.getBeansOfType(type);
    }

    /**
     * 获取当前环境
     */
    public static String getActiveProfile() {
        return applicationContext.getEnvironment().getActiveProfiles()[0];
    }

    /**
     * 获取配置属性值
     *
     * @param property 属性名
     */
    public static String getPropertyValue(String property) {
        return getPropertyValue(property, null);
    }

    /**
     * 带默认值的获取配置属性值
     *
     * @param property     属性名
     * @param defaultValue 默认值
     */
    public static String getPropertyValue(String property, String defaultValue) {
        return applicationContext.getEnvironment().getProperty(property, defaultValue);
    }

    /**
     * 获取指定的数据类型
     *
     * @param property     属性名
     * @param clazz        值类型
     * @param defaultValue 默认值
     */
    public static <T> T getPropertyValue(String property, Class<T> clazz, T defaultValue) {
        return applicationContext.getEnvironment().getProperty(property, clazz, defaultValue);
    }

}
