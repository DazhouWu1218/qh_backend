package com.njht.webyun.management.dataanalysis.systeminformation.service;

import com.njht.webyun.management.dataanalysis.systeminformation.entity.CimissGribDataFileInfoEntity;

import java.util.List;

/**
 * (CimissGribDataFileInfo)表服务接口
 *
 * @author makejava
 * @since 2021-06-29 09:57:02
 */
public interface PretreatmentDataService {


    /**
     * 获取格点数据预处理结果文件大小
     *
     * @return
     */
    Long getPretreatmentFileSizeCount();


    /**
     * 定时刷新预处理数据量统计
     */
    void refreshPretreatmentFileSizeCount();


    /**
     * 补充cimiss_grib_data_file_info file_size 属性
     */
    void fillCmissGribDataFile();


    /**
     * @param filePath
     */
    void fillSigleGribDataFileSize(CimissGribDataFileInfoEntity cimissGribDataFileInfoEntity);

}
