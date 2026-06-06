package com.htht.executor.task.service.base.impl;

import com.google.common.base.Objects;
import com.htht.executor.task.constant.BaseProductServiceConstant;
import com.htht.executor.task.service.BaseProductHandlerService;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.entity.xml.InputXmlParamDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: 代国军
 * @CreateDate: 2021/10/29 9:28
 * @Description: 输入xml中要求有多个输入信息,输入信息只做校验，不需要进行正则匹配等操作
 * 处理输入参数map 以循环的方式写进inputXml中
 */
@Service("baseManyInputInfoHandlerService")
@Slf4j
public class BaseManyInputInfoHandlerService extends BaseProductHandlerService {


    @Override
    public String addInputXmlParam(TriggerParam triggerParam, ProductParam productParam, InputXmlParamDTO inputXmlParam) {
        String issue = triggerParam.getExecutorParams();
        log.info("---BaseMoreInputInfoHandlerService,获取输入文件信息====={}",issue);
        //没有正则输入文件只有路径,有正则输入文件为匹配到的具体文件
        String inputInfo = super.getInputInfo(issue, productParam.getFileNamePattern(),productParam.getInputFilePath());
        if(inputInfo == null){
            return BaseProductServiceConstant.No_Input_Msg + ":" + productParam.getInputFilePath();
        }
        inputXmlParam.setInputFile(inputInfo);
        //获取页面的输入参数
        LinkedHashMap dyMap = triggerParam.getTaskParam().getDynamicMap();
        Map otherMap = new HashMap(16);
        dyMap.forEach((s,s2) -> {
            //常用的五个输入参数不写进输入xml中
            if(Boolean.TRUE.equals(!isInputFileInfo(((String)s)) && StringUtils.isNotEmpty((String)s2))){
                otherMap.put(s,s2);
            }
        });
        inputXmlParam.setOtherMap(otherMap);
        return null;
    }

    /**
     * 判断输入xml中的输入参数信息
     * @param s
     * @return
     */
    private Boolean isInputFileInfo(String s) {
        return Objects.equal(s,BaseProductServiceConstant.Input_pgsqlTab) ||
                Objects.equal(s,BaseProductServiceConstant.Input_businessService) ||
        Objects.equal(s,BaseProductServiceConstant.Input_searchType) ||
        Objects.equal(s,BaseProductServiceConstant.Input_productKey) ||
        Objects.equal(s,BaseProductServiceConstant.Input_resolution);
    }
}
