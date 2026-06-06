package com.htht.executor.satellite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.executor.satellite.entity.SateDataTaskFileEntity;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.entity.paramtemplate.PreDataParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 卫星数据在入库流程中,文件的记录表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-19 11:40:19
 */
public interface SateDataTaskFileService extends IService<SateDataTaskFileEntity> {

    /**
     * 查找文件名 个数
     * @param fileName
     * @return
     */
    long selectCountByFileName(String fileName);

    /**
     * 根据卫星名称 传感器 分辨率 去重
     * @param preDataParam
     * @return
     */
    List<String> selectFileNameBySatellite(PreDataParam preDataParam);

    /**
     * 预处理信息入库
     * @param preDataParam
     * @param dyMap
     * @param returnMap
     */
    void savePreDataInfo(PreDataParam preDataParam, LinkedHashMap<String, Object> dyMap, Map<String, String> returnMap
                        , TriggerParam triggerParam,String satelliteUrl);

    /**
     * h8 预处理结果入库
     * @param originPath
     * @param outputXml
     * @param issue
     * @param triggerParam
     */
    void saveH8ProjectToDB(String originPath, String outputXml, String issue, TriggerParam triggerParam,String satelliteUrl);

}

