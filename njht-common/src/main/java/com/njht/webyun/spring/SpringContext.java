package com.njht.webyun.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Author chengqh
 * @Date 2020/11/19
 */

public class SpringContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
    {
        SpringContext.applicationContext = applicationContext;
    }

    public static Object getBean(String name)
    {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType)
    {
        return applicationContext.getBean(requiredType);
    }
}
