package com.htht.job.admin.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 现场安全变量
 */
public class ThreadLocalParams {

    public static ThreadLocal<List> productIds = ThreadLocal.withInitial(() -> new ArrayList());
}
