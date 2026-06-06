package com.njht.webyun.management.dataanalysis.systeminformation.dao;

import com.njht.webyun.management.dataanalysis.systeminformation.entity.CimissGribDataFileInfoEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (CimissGribDataFileInfo)表数据库访问层
 *
 * @author makejava
 * @since 2021-06-29 09:57:01
 */
@Repository
public interface PretreatmentDataDao {

    /**
     * 根据数据类型获取格点数据
     *
     * @param dataType
     * @return
     */
    List<CimissGribDataFileInfoEntity> getByDataType(Integer dataType);


    /**
     * 根据数据类型获取格点数据
     *
     * @param dataType
     * @return
     */
    Long countFileSizeByDataType(Integer dataType);


    /**
     * 统计所有 预处理的卫星数据
     *
     * @return
     */
    Long countAllPretreatmentSateData();


    List<CimissGribDataFileInfoEntity> getEmptyFileSizeFilePath(@Param("dataType") Integer dataType);


    void updateCimssGribDataFileSize(@Param("id") String id, @Param("fileSize") Long fileSize);


}

