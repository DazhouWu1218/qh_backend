package com.njht.webyun.management.dataanalysis.systemvisit.constant;

public class SystemVisitCountConstant {

    /**
     * 用户的访问计数信息 redis前缀
     */
    public static final String SYSTEM_LOG_VISIT_COUNT_USER_COUNT_REDIS_SUFFIX = "systemLog:visitCount:userVisitRecord:";

    /**
     * 系统所有历史访问总数
     * redis key
     */
    public static final String SYSTEM_LOG_VISIT_COUNT_ALL_HISTORY_COUNT_REDIS_KEY = "systemLog:visitCount:allHistoryCount";


    /**
     * 系统所有历史访问总数
     */
    public static final String SYSTEM_LOG_VISIT_COUNT_WEEK_MONTH_YEAR = "systemLog:visitCount:weekMonthYear";

}
