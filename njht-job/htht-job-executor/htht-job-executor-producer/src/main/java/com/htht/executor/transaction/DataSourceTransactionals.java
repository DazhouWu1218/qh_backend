package com.htht.executor.transaction;

import java.lang.annotation.*;

/**
 * 事务注解
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DataSourceTransactionals {

    /**
     * 事务管理器数组
     */
    String[] transactionManagers();
}
