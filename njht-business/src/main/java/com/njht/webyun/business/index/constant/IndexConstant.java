package com.njht.webyun.business.index.constant;

/**
 * @Author: 代国军
 * @CreateDate: 2022/1/6 19:33
 * @Description: 首页相关常量
 */
public class IndexConstant {

    private IndexConstant() {}

    /**
     * 周期类型（1-本日，2-本周，3-本月  today weekly monthly）
     */
    public static final String INDEX_ONE = "today";
    public static final String INDEX_TWO = "weekly";
    public static final String INDEX_THREE = "monthly";
    public static final String INDEX_ALL = "all";

    /**
     * 生产标识（异常 failed 正常 success 执行中 executing）
     */
    public static final String INDEX_PRODUCT_FAILED = "failed";
    public static final String INDEX_PRODUCT_SUCCESS = "success";
    public static final String INDEX_PRODUCT_EXECUTING = "executing";
    public static final String INDEX_PRODUCT_ALL = "all";



    /**
     * 任务监控
     */
    public static final String INDEX_TASK_SUCCESS_MSG = "执行成功";
    public static final String INDEX_TASK_FAIL_MSG = "执行异常";
    public static final String INDEX_TASK_RUN_MSG = "执行中";

    public static final String INDEX_PARENT_ID = "0";



}
