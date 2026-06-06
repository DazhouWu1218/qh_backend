package com.htht.executor.task.service;


import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.entity.xml.InputXmlParamDTO;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 基础产品处理流程接口
 * 2022-03-25
 * @author zedong
 */
public interface BaseProductInterface {
    /**
     * 执行分片任务
     * @param taskParam 前端传参
     * @return
     */
    ReturnT<List<String>> executeShard(TaskParam taskParam);

    /**
     * 算法调度执行
     * @param triggerParam
     * @return
     */
    ReturnT<String> execute(final TriggerParam triggerParam);



    /**
     * 添加输入xml文件
     * @param triggerParam
     * @param productParam
     * @param inputXmlParam
     * @return
     */
    default String addInputXmlParam(final TriggerParam triggerParam, final ProductParam productParam, InputXmlParamDTO inputXmlParam){
        return null;
    }
    /**
     * 产品统计数据保存到数据库中
     * @param filePath 文件路径
     * @param fileType 文件类型
     * @param regionId  区域ID
     */
    default void saveStatisticDataToDb(String filePath, String fileType, String regionId) {
    }

    default void getIssueList(List<String> issueList, ProductParam productParam, LinkedHashMap dyMap, String s){

    }

    default LinkedHashMap<String,Object> executeFixMap(String issue){
        return  XxlJobHelper.getTriggerParam().getTaskParam().getFixedMap();
    }
}
