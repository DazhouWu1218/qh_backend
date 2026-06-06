package com.htht.executor.satellite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.executor.satellite.entity.SateDataInfoEntity;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.entity.paramtemplate.PreDataParam;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-19 11:40:19
 */
public interface SateDataInfoService extends IService<SateDataInfoEntity> {

    /**
     * 卫星数据基本信息入库
     * @param targetFile
     * @param dyMap
     * @param returnMap
     * @param preDataParam
     * @param triggerParam
     * @return
     */
    SateDataInfoEntity saveSateDataInfo(File targetFile, LinkedHashMap<String, Object> dyMap, Map<String, String> returnMap,
                                        PreDataParam preDataParam, TriggerParam triggerParam);

    /**
     * h8预处理入库
     * @param originFile
     * @param outputXml
     * @param triggerParam
     * @return
     */
    SateDataInfoEntity saveH8SateDataInfo(File originFile, String outputXml, TriggerParam triggerParam,String issue);

    /**
     * 查询总数
     * @param satelliteId
     * @param sensorId
     * @param day
     * @param hour
     * @return
     */
    Integer selectCountSate(String satelliteId, String sensorId, String day, String hour);
}

