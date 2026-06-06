package com.htht.job.admin.param.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htht.job.admin.dispatch.core.trigger.XxlJobTrigger;
import com.htht.job.admin.dispatch.service.DataCategoryService;
import com.htht.job.admin.dispatch.vo.JobReqVo;
import com.htht.job.admin.param.dao.TaskParametersDao;
import com.htht.job.admin.param.service.TaskParamService;
import com.htht.job.admin.template.vo.TemplateParamReqVo;
import com.htht.job.core.biz.model.TaskParametersEntity;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.exception.CommonException;
import com.htht.job.core.handler.annotation.XxlJob;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author 代国军
 * @description: 任务参数
 * @date 2022/6/23 10:13
 */
@Service
public class TaskParamServiceImpl implements TaskParamService {


    @Autowired
    private TaskParametersDao taskParametersDao;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     *
     * @param infoDto
     */
    @Override
    public void updateParams(JobReqVo infoDto) {
        TaskParametersEntity taskParametersEntity = new TaskParametersEntity();
        BeanUtils.copyProperties(infoDto,taskParametersEntity);
        taskParametersEntity.setJobId(infoDto.getId()); // jobId 与 主键id 关联
        try {
            this.setParamInfo(taskParametersEntity,infoDto);
        } catch (JsonProcessingException e) {
            throw new CommonException(e.getMessage());
        }
        this.executeProductInfo(infoDto, taskParametersEntity);
		taskParametersDao.update(taskParametersEntity);
    }

    @Autowired
    private DataCategoryService dataCategoryService;

    @Override
    public void saveParams(JobReqVo infoDto) {
        TaskParametersEntity taskParametersEntity = new TaskParametersEntity();
        BeanUtils.copyProperties(infoDto,taskParametersEntity);
        try {
            this.setParamInfo(taskParametersEntity,infoDto);
        } catch (JsonProcessingException e) {
            throw new CommonException(e.getMessage());
        }
        taskParametersEntity.setJobId(infoDto.getId()); // jobId 与 主键id 关联
        taskParametersEntity.setId(UUID.randomUUID().toString().replace("-",""));
        this.executeProductInfo(infoDto, taskParametersEntity);
        taskParametersDao.insert(taskParametersEntity);
    }

    private void executeProductInfo(JobReqVo infoDto, TaskParametersEntity taskParametersEntity) {
        // 根据treeId 获取 product相关信息
        String productId = dataCategoryService.getProductId(infoDto.getTreeId());
        taskParametersEntity.setProductId(productId);

        // 获取周期数据源信息
        ProductParam param = this.getCycleAndDataSource(infoDto.getModelParameterList());
        taskParametersEntity.setCycle(param.getCycle());
        taskParametersEntity.setSatellite(param.getSatellite());
        taskParametersEntity.setSensor(param.getSensor());
    }

    /**
     * 获取周期和数据源
     * @param modelParameterList
     * @return
     */
    private ProductParam getCycleAndDataSource(List<TemplateParamReqVo> modelParameterList) {
        String paramJsonStr = XxlJobTrigger.getParamJsonStr(JSON.toJSONString(modelParameterList));
        return JSONObject.parseObject(paramJsonStr,ProductParam.class);
    }

    @Override
    public void deleteByJobId(int id) {
        taskParametersDao.deleteByJobId(id);
    }

    /**
     * 设置模板参数相关值
     * @param taskParametersEntity
     * @param infoDto
     * @throws JsonProcessingException
     */
    private void setParamInfo(TaskParametersEntity taskParametersEntity,JobReqVo infoDto) throws JsonProcessingException {
        //  动态参数
        if( infoDto.getDynamicList() != null && !infoDto.getDynamicList().isEmpty() ){
            taskParametersEntity.setDynamicParameter(objectMapper.writeValueAsString(infoDto.getDynamicList()));
        } else {
            taskParametersEntity.setDynamicParameter(objectMapper.writeValueAsString(new ArrayList<>()));
        }
        // 算法参数
        if (infoDto.getFixedList() != null && !infoDto.getFixedList().isEmpty()) {
            taskParametersEntity.setFixedParameter(objectMapper.writeValueAsString(infoDto.getFixedList()));
        } else {
            taskParametersEntity.setFixedParameter(objectMapper.writeValueAsString(new ArrayList<>()));
        }

        // 调度模板参数
        if ( infoDto.getModelParameterList() != null && !infoDto.getModelParameterList().isEmpty()) {
            taskParametersEntity.setModelParameters(objectMapper.writeValueAsString(infoDto.getModelParameterList()));
        }

    }
}
