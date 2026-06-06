package com.njht.webyun.publish.behavior.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njht.webyun.publish.behavior.entity.BehaviorDownloadLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-12-08 09:09:54
 */
@Mapper
public interface BehaviorDownloadLogDao extends BaseMapper<BehaviorDownloadLogEntity> {
    /**
     * 查询统计结果
     * @param endTime
     * @param weekStartTime
     * @param monStartTime
     * @param yearStartTime
     * @return
     */
    Map<String,Long> selectCountInfo(@Param("endTime") Date endTime,
                                     @Param("weekStartTime")Date weekStartTime,
                                     @Param("monStartTime")Date monStartTime,
                                     @Param("yearStartTime")Date yearStartTime);

    /**
     * 查询文件个数
     * @param startTime
     * @param endTime
     * @return
     */
    List<BehaviorDownloadLogEntity> selectFileNumList(@Param("startTime")Date startTime,
                                              @Param("endTime")Date endTime);

    /**
     * 统计文件下载个数
     * @return
     */
    Integer selectFileCountInfo();
}
