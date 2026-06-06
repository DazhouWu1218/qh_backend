package com.njht.webyun.management.dataanalysis.systemdatacount.service;


import com.njht.webyun.management.dataanalysis.systemdatacount.dto.LogSystemDataCountByDayDTO;
import com.njht.webyun.management.dataanalysis.systemdatacount.vo.LogSystemDataByDataTypeVO;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 系统数据接入,下载统计日志
 *
 * @author zhouhouliang
 */
public interface LogSystemDataCountService {

    /**
     * 累加系统各类数据的 接入,下载信息
     *
     * @param dataName
     * @param dataSize
     */
    Long incrementSystemDataDownload(String dataName, Long dataSize);


    /**
     * 更新或者新增 系统数据统计信息
     *
     * @param isDownload
     * @param dateSize
     */
    void updateOrInsertSystemDataCount(Boolean isDownload, Long categoryId, Date date, Long dateSize);


    /**
     * 统计近一周的数据
     *
     * @return
     */
    List<LogSystemDataCountByDayDTO> countWeek(boolean isDownload);

    /**
     * 统计近一月的数据
     *
     * @param isDownload
     * @return
     */
    List<LogSystemDataCountByDayDTO> countMonth(boolean isDownload);

    /**
     * 统计近一年的数据
     *
     * @param isDownload
     * @return
     */
    List<LogSystemDataCountByDayDTO> countYear(boolean isDownload);


    /**
     * 累计接收,累计分发
     *
     * @param isDownload
     * @return
     */
    Long countAllHistory(boolean isDownload);


    /**
     * 系统中各类数据 按照数据类型统计汇总
     *
     * @param isDownload
     * @return
     */
    List<LogSystemDataByDataTypeVO> statisticByDataType(Boolean isDownload);


    /**
     * 根据要素获取数据所属分类
     *
     * @param isDownload
     * @param dataName
     * @return
     */
    Long getCategoryId(@NotNull boolean isDownload, @NotNull String dataName);




}
