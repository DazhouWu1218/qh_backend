package com.htht.executor.task.service.base.impl;

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
 * @CreateDate: 2021/10/26 17:45
 * @Description: 同一个期次多个输入文件情况,且输入文件都需要进行正则匹配
 */
@Service("baseTwoInputFileHandlerService")
@Slf4j
public class BaseTwoInputFileHandlerService extends BaseProductHandlerService {
    @Override
    public String addInputXmlParam(TriggerParam triggerParam, ProductParam productParam, InputXmlParamDTO inputXmlParam) {
        String issue = triggerParam.getExecutorParams();
        log.info("---BaseTwoInputFileHandlerService,获取输入文件信息====={}",issue);
        String inputFile1 = productParam.getInputFilePath();
        String regex = productParam.getFileNamePattern();
        String filePath = super.getInputInfo(issue,regex,inputFile1);
        if(filePath!=null && !filePath.isEmpty()){
            inputXmlParam.setInputFile(filePath);
        }else{
            return BaseProductServiceConstant.No_Input_Msg + ":" + inputFile1;
        }
        //获取输入文件中的其他文件信息
        LinkedHashMap<String,Object> dynamicMap = triggerParam.getTaskParam().getDynamicMap();
        String otherFilePath = (String) dynamicMap.get("otherFilePath");
        String otherFileRegex = (String)dynamicMap.get("otherFileRegex");
        String otherFileName= (String)dynamicMap.get("otherFileName");
        if(StringUtils.isNotEmpty(otherFilePath)){
            String otherFilePaths = super.getInputInfo(issue,otherFileRegex,otherFilePath);
            if(otherFilePaths != null ){
                Map otherMap = new HashMap();
                otherMap.put(otherFileName,otherFilePaths);
                inputXmlParam.setOtherMap(otherMap);
            }else {
                return BaseProductServiceConstant.No_Input_Msg + ":" + otherFilePath;
            }
        }
        return null;
    }



}
