package com.htht.job.core.util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 多线程配置类
 * 启动异步方法注解
 * @author zedong
 */
@Slf4j
public class SyncDataThreadPool {

    private SyncDataThreadPool(){

    }

    private static class SingletonHolder {

        /**
         * 每秒需要多少个线程处理?
         * tasks/(1/taskcost)
         */
        private static int corePoolSize = 10;

        /**
         * 线程池维护线程的最大数量
         * (max(tasks)- queueCapacity)/(1/taskcost)
         */
        private static int maxPoolSize = 20;

        /**
         * 缓存队列
         * (coreSizePool/taskcost)*responsetime
         */
        private static int queueCapacity = 1000;

        /**
         * 允许的空闲时间
         * 默认为60
         */
        private static int keepAlive = 100;


        private static TaskExecutor threadPool() {
            log.info(">>>>>>>>>>> pie-job SyncDataThreadPool init");
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            // 设置核心线程数
            executor.setCorePoolSize(corePoolSize);
            // 设置最大线程数
            executor.setMaxPoolSize(maxPoolSize);
            // 设置队列容量
            executor.setQueueCapacity(queueCapacity);
            // 设置允许的空闲时间（秒）
            executor.setKeepAliveSeconds(keepAlive);
            // 设置默认线程名称
            executor.setThreadNamePrefix("thread-");
            // 设置拒绝策略rejection-policy：当pool已经达到max size的时候，如何处理新任务
            // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            // 等待所有任务结束后再关闭线程池
            executor.setWaitForTasksToCompleteOnShutdown(true);
            executor.initialize();
            return executor;
        }
    }

    public static TaskExecutor newInstance(){
        return SingletonHolder.threadPool();
    }
}
