package com.htht.executor.task.util;

import com.alibaba.fastjson.JSON;
import com.htht.executor.satellite.entity.SateDataInfoEntity;
import com.htht.executor.task.constant.PreProcessConstant;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.entity.paramtemplate.PreDataParam;
import com.htht.job.core.exception.CommonException;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author 代国军
 * @description: 预处理 工具类
 * @date 2022/5/23 10:35
 */
public class PreJobUtil {

    /**
     * 获取卫星 传感器 分辨率
     * @param preDataParam
     * @return
     */
    public static SateDataInfoEntity setSatelliteInfo(PreDataParam preDataParam) {
        SateDataInfoEntity sateDataInfoEntity = new SateDataInfoEntity();
        String[] ssr = preDataParam.getPreDataTaskName().split("_");
        String satellite = "";
        String sensor = "";
        String resolution = "";
        if (ssr.length == PreProcessConstant.THREE) {
            satellite = ssr[0];
            sensor = ssr[1];
            resolution = ssr[2];
        }
        if (PreProcessConstant.GLL.equals(preDataParam.getProjectionIdentify())) {
            String x = StringUtils.isEmpty(preDataParam.getResolutionX()) ? "0.01": preDataParam.getResolutionX();
            resolution = Integer.toString((int) (Double.parseDouble(x) * 100000));
        } else {
            resolution = preDataParam.getResolutionX();
        }
        sateDataInfoEntity.setSatelliteId(satellite);
        sateDataInfoEntity.setSensorId(sensor);
        sateDataInfoEntity.setResolution(Double.valueOf(resolution));
        return sateDataInfoEntity;
    }


    /**
     * 获取预处理参数
     * @param taskParam
     * @return
     */
    public static PreDataParam getPreDataParam(TaskParam taskParam) {
        // 合并调度参数和算法参数 为map
        LinkedHashMap<String, Object> fixedMap = taskParam.getFixedMap();
        PreDataParam preDataParam = null;
        try {
            //获取到调度参数
            HashMap hashMap = JSON.parseObject(taskParam.getModelParameters(), HashMap.class);
            fixedMap.putAll(hashMap);
            // 获取返回结果
            preDataParam = JSON.parseObject(JSON.toJSONString(fixedMap),PreDataParam.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException("预处理参数获取失败,"+e.getMessage());
        }
        return preDataParam;
    }
}
