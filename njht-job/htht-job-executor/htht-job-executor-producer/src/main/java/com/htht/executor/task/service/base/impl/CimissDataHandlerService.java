package com.htht.executor.task.service.base.impl;

import com.htht.executor.product.service.ProductService;
import com.htht.executor.task.constant.BaseProductServiceConstant;
import com.htht.executor.task.service.BaseProductHandlerService;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.entity.xml.InputXmlParamDTO;
import com.htht.job.core.util.FileNameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: 代国军
 * @CreateDate: 2021/10/26 17:45
 * @Description: 从cimss 表读取数据生成txt
 */
@Service("cimissDataHandlerService")
@Slf4j
public class CimissDataHandlerService extends BaseProductHandlerService {

    @Autowired
    private ProductService productService;

    @Override
    public String addInputXmlParam(TriggerParam triggerParam, ProductParam productParam, InputXmlParamDTO inputXmlParam) {
        String issue = triggerParam.getExecutorParams();
        log.info("---BaseTwoInputFileHandlerService,获取输入文件信息====={}",issue);
        // 获取输入文件
        String regex = productParam.getFileNamePattern();
        String inputFile1 = productParam.getInputFilePath();
        String filePath = super.getInputInfo(issue,regex,inputFile1);
        if(filePath!=null && !filePath.isEmpty()){
            inputXmlParam.setInputFile(filePath);
        }else{
            return BaseProductServiceConstant.No_Input_Msg + ":" + inputFile1;
        }
        // 生成 txt
        log.info("开始生成 cimiss txt");
        LinkedHashMap<String, Object> dynamicMap = triggerParam.getTaskParam().getDynamicMap();
        String txtFilePath = (String) dynamicMap.get("txtFile");
        txtFilePath = FileNameUtils.dealFilePath(txtFilePath,issue);
        log.info("cimiss- txt 存储路径{}",txtFilePath);
        String txtCols = (String) dynamicMap.get("txtCols");
        String tableName = (String) dynamicMap.get("tableName");
        String txtName = (String)dynamicMap.get("txtName");
        // 查询数据信息
        List<Map<String,Object>> list = productService.selectCimisInfoFromTable(txtCols,tableName,issue,productParam.getCycle());
        // 根据查询结果生成txt
        this.saveToTxt(txtCols,list,txtFilePath);
        Map otherMap = new HashMap();
        otherMap.put(txtName,txtFilePath);
        inputXmlParam.setOtherMap(otherMap);
        return null;
    }




}
