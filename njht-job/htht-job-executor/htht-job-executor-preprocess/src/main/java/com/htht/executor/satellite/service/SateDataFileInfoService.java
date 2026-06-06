package com.htht.executor.satellite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.executor.satellite.entity.SateDataFileInfoEntity;
import com.htht.executor.satellite.entity.SateDataInfoEntity;
import com.htht.executor.satellite.entity.SateDataTaskFileEntity;
import com.htht.job.core.biz.model.TriggerParam;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-19 11:40:19
 */
public interface SateDataFileInfoService extends IService<SateDataFileInfoEntity> {

    /**
     * 预处理文件信息入库
     * @param sateDataInfoEntity
     * @param sateDataTaskFileEntity
     * @param dyMap
     * @param returnMap
     * @param triggerParam
     * @param satelliteUrl
     */
    void saveSateFileInfo(SateDataInfoEntity sateDataInfoEntity, SateDataTaskFileEntity sateDataTaskFileEntity,
                          LinkedHashMap<String, Object> dyMap, Map<String, String> returnMap, TriggerParam triggerParam, String satelliteUrl);

    /**
     * h8预处理文件信息入库
     * @param originFile
     * @param outputXml
     * @param triggerParam
     * @param taskFileEntity
     * @param sateDataInfoEntity
     * @param satelliteUrl
     */
    void saveH8SateDataFileInfo(File originFile, String outputXml, TriggerParam triggerParam, SateDataTaskFileEntity taskFileEntity, SateDataInfoEntity sateDataInfoEntity, String satelliteUrl);
}

