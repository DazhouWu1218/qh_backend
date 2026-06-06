package com.njht.webyun.management.dataanalysis.systemvisit.service;

import com.njht.webyun.management.dataanalysis.systemvisit.dto.SystemVisitDateCountDTO;
import com.njht.webyun.management.dataanalysis.systemvisit.entity.SystemVisitCountEntity;

import java.util.List;
import java.util.Map;

public interface SystemVisitCountService {


    /**
     * 添加访问次数
     */
    Long incrementCount();

    /**
     * 获取访问总量
     */
    Long selectAllCount();


    /**
     * 所有用户访问量+1
     *
     * @return
     */
    Long incrementAllCount();

    /**
     * 根据 用户和时间,获取用户该日的访问记录
     *
     * @param userId
     * @param dateStr
     * @return
     */
    SystemVisitCountEntity selectByUserIdAndDate(String userId, String dateStr);


    /**
     * 更新或者插入一条新的用户访问数据
     *
     * @param userId
     * @param dateStr
     * @param count
     */
    void insertOrUpdateSystemVisitCount(String userId, String dateStr, Long count);


    /**
     * 用户访问统计
     * 周月年
     *
     * @return
     */
    Map<String, List<SystemVisitDateCountDTO>> weekMonthYearStatistics();

}
