package com.htht.job.core.shard;

import com.htht.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * 从容器中获取对应的Handler信息
 * @author daiguojun
 */
@Slf4j
public class SpringContextUtil {

    /**
     * 获取对象
     * @param beanId bean名称
     * @return
     * @throws BeansException
     */
    public static Object getBean(String beanId) throws BeansException {
        ApplicationContext applicationContext = XxlJobSpringExecutor.getApplicationContext();
        try {
            return applicationContext.getBean(beanId);
        } catch (BeansException e) {
            log.error(">>>>>>>>>>> xxl-job shard trigger info {}",e.getMessage());
            return null;
        }
    }

    /**
     * 获取对象
     * @param clazz 类名
     * @return
     * @throws BeansException
     */
    public static Object getBeanByType(Class clazz) throws BeansException {
        ApplicationContext applicationContext = XxlJobSpringExecutor.getApplicationContext();
        try {
            return applicationContext.getBean(clazz);
        } catch (BeansException e) {
            log.error(">>>>>>>>>>> xxl-job shard trigger error {} >>>>>>>>>>",e.getMessage());
            return null;
        }
    }

}
