package com.njht.webyun.management.dataanalysis.systemdatacount.constant;

public class SystemDataCountConstant {


    /**
     * 系统各类数据统计 redis 前缀
     */
    public static final String SYSTEM_DATA_COUNT_RECORD_REDIS_SUFFIX = "systemLog:DataCount:record:";


    /**
     * 系统各类数据接收/发放统计
     * 周月年 统计
     * 缓存方法 redis前缀
     */
    public static final String SYSTEM_DATA_COUNT_TIME_STATISTICS_METHOD = "systemLog:DataCount:method:timeStatistics:";

    /**
     * 系统各类数据接收/发放统计
     * 所有类型数据总和
     * 缓存方法 redis前缀
     */
    public static final String SYSTEM_DATA_COUNT_ALL_HISTORY_COUNT_METHOD = "systemLog:DataCount:method:allHistoryCount:";


    /**
     * 系统各类数据按照数据类别汇总统计
     * 缓存方法 redis前缀
     */
    public static final String SYSTEM_DATA_COUNT_COUNT_BY_DATA_TYPE_METHOD = "systemLog:DataCount:method:countByType:";


}
