package com.njht.webyun.management.dataanalysis.systemdatacount.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njht.webyun.management.dataanalysis.systemdatacount.dto.LogSystemDataCountByDayDTO;
import com.njht.webyun.management.dataanalysis.systemdatacount.dto.LogSystemDataCountDTO;
import com.njht.webyun.management.dataanalysis.systemdatacount.entity.LogSystemDataCountEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface LogSystemDataCountDao extends BaseMapper<LogSystemDataCountEntity> {


    /**
     * 插入一条记录
     */
    void insertNew(@Param("categoryId") Long categoryId,
                   @Param("date") Date date,
                   @Param("dataSize") long dataSize);


    /**
     * 根据分类和日期更新dataSize
     *
     * @param categoryId
     * @param dataSize
     */
    void updateDataSizeByCategoryIdAndDate(@Param("categoryId") long categoryId, @Param("date") Date date, @Param("dataSize") long dataSize);

    /**
     * 查询一个统计记录
     * 根据分类和日期
     *
     * @param categoryId
     * @param date
     * @return
     */
    LogSystemDataCountDTO selectByCategoryAndDate(@Param("categoryId") Long categoryId, @Param("date") Date date);


    /**
     * 根据dataName寻找分类id
     *
     * @param dataName
     * @param isDownload
     * @return
     */
    Long selectDataTypeByDataName(@Param("dataName") String dataName, @Param("isDownload") Boolean isDownload);


    /**
     * 按照天 求和数据大小
     *
     * @param assertDownload
     * @param startDay
     * @param endDay
     * @return
     */
    List<LogSystemDataCountByDayDTO> selectSystemDataCountByDay(@Param("assertDownload") boolean assertDownload, @Param("startDay") Date startDay, @Param("endDay") Date endDay);

    /**
     * 统计所有的发布/下载的数据
     *
     * @param assertDownload
     * @return
     */
    Long countAllHistory(@Param("assertDownload") boolean assertDownload);


    /**
     * 系统中各类数据 按照数据类型统计汇总
     *
     * @param isDownload
     * @return
     */
    List<LogSystemDataCountDTO> statisticByDataType(Boolean isDownload);





}
