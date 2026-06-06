package com.htht.job.core.util;

import com.htht.job.core.biz.model.TriggerParam;

/**
 * ThreadLocal线程安全工具类
 */
public class ThreadLocalUtil {
    private ThreadLocalUtil() {

    }

    public static ThreadLocal<TriggerParam> triggerParamThreadLocal = ThreadLocal.withInitial(() -> new TriggerParam());
}
