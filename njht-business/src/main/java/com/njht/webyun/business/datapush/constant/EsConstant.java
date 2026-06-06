package com.njht.webyun.business.datapush.constant;

/**
 * @author daiguojun
 * @date 2022-09-06 15:18
 * 推送数据类型枚举
 */
public class EsConstant {

    /**
     * 数据推送url
     */
    public static final String DI_URL = "http://10.129.89.181/store/openapi/v2/logs/push_batch?apikey=e10adc3949ba59abbe56e057f2gg88dd";

    private EsConstant() {
        
    }

    /**
     * 系统运行状态
     */
    public static final String SYSTEM_STATUS = "0";
    /**
     * 数据到达统计
     */
    public static final String DATA_COLLECT = "1";

    /**
     * 产品生产情况统计
     */
    public static final String PRODUCT_COLLECT = "2";

    /**
     * 任务执行情况统计
     */
    public static final String TASK_EXECUTE = "3";

    /**
     * 单个任务失败告警
     */
    public static final String SINGLE_TASK_EI = "4";

    /**
     * 服务类型
     */
    public static final String SYSTEM = "SYSTEM";
    public static final String REDIS = "REDIS";
    public static final String MYSQL = "MYSQL";

    /**
     * 正常 1 不正常 0
     */
    public static final String NORMAL = "正常";
    public static final String ABNORMAL = "不正常";

    /**
     * 天境推送地址
     */
    public static final String BUSINESS_DI_EI_URL = "BUSINESS_DI_EI_URL";

    /**
     * 天境数据ES类型
     */
    public static final String BUSINESS_DI_TYPE = "BUSINESS_DI_TYPE";

    public static final String BUSINESS_EI_TYPE = "BUSINESS_EI_TYPE";

    /**
     * 统计周期
     */
    public static final String STATISTIC_CYCLE = "DAY";
}
