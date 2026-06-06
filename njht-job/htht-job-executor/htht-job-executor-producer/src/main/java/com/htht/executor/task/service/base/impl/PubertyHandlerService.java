package com.htht.executor.task.service.base.impl;

import com.htht.executor.product.service.ProductService;
import com.htht.executor.task.constant.BaseProductServiceConstant;
import com.htht.executor.task.enums.CimissTxtEnum;
import com.htht.executor.task.service.BaseProductHandlerService;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.entity.xml.InputXmlParamDTO;
import com.htht.job.core.exception.CommonException;
import com.htht.job.core.util.FileNameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: 代国军
 * @CreateDate: 2021/10/26 17:45
 * @Description:  作物发育期 插件
 */
@Service("pubertyHandlerService")
@Slf4j
public class PubertyHandlerService extends BaseProductHandlerService {

    @Autowired
    private ProductService productService;


    @Override
    public String addInputXmlParam(TriggerParam triggerParam, ProductParam productParam, InputXmlParamDTO inputXmlParam) {
        String issue = triggerParam.getExecutorParams();
        log.info("---BaseTwoInputFileHandlerService,获取输入文件信息====={}",issue);
        // 获取输入文件
        String inputFile1 = productParam.getInputFilePath();
        String regex = productParam.getFileNamePattern();
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

        Map otherMap = new HashMap();
        //  txtCols_Grow
        String txtColsGrow = (String) dynamicMap.get(CimissTxtEnum.txtCols_Grow.getDesc());
        // 该数据报存路径
        String growPath = txtFilePath + File.separator + CimissTxtEnum.txtCols_Grow.getDesc()+issue+".txt";
        if (!this.existFile(growPath)) {
            executeToTxt(issue, growPath, CimissTxtEnum.txtCols_Grow.getTableName(), txtColsGrow);
        }
        otherMap.put(CimissTxtEnum.txtCols_Grow.getDesc(),growPath);
        // txtCols_Day
        String txtColsDay = (String) dynamicMap.get(CimissTxtEnum.txtCols_Day.getDesc());
        // 该数据报存路径
        String colsDayPath = txtFilePath + File.separator + CimissTxtEnum.txtCols_Day.getDesc()+issue+".txt";
        if (!this.existFile(colsDayPath)) {
            executeToTxt(issue, colsDayPath, CimissTxtEnum.txtCols_Day.getTableName(), txtColsDay);
        }
        otherMap.put(CimissTxtEnum.txtCols_Day.getDesc(),colsDayPath);

        inputXmlParam.setOtherMap(otherMap);
        return null;
    }

    /**
     * 判断文件是否存在
     * @param path
     * @return
     */
    private boolean existFile(String path) {
        File file = new File(path);
        return file.exists() && file.length() != 0L;
    }

    /**
     * 查询数据并生成txt
     * @param issue
     * @param txtFilePath
     * @param tableName
     * @param txtColsGrow
     */
    private void executeToTxt(String issue, String txtFilePath, String tableName,String txtColsGrow) {
        // 查询数据信息
        List<Map<String,Object>> list = productService.selectCimisInfoFromTable(txtColsGrow,tableName,issue, "COOD");
        if (list.isEmpty()) {
            XxlJobHelper.log("数据库没有该期次:{},的数据",issue);
            throw new CommonException("数据库没有该期次相关的日资料数据");
        }
        // 根据查询结果生成txt
        this.saveToTxt(txtColsGrow,list,txtFilePath);
    }



}
