package com.htht.executor.task.service.base.impl;

import com.htht.executor.task.service.BaseProductHandlerService;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.entity.xml.InputXmlParamDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Service(value="forecastSnowH9HandlerService")
@Slf4j
public class ForecastSnowH9HandlerService extends BaseProductHandlerService {

    @Override
    public String addInputXmlParam(TriggerParam triggerParam, ProductParam productParam, InputXmlParamDTO inputXmlParam) {
        String issue = triggerParam.getExecutorParams();
        log.info("---forecastSnowH9HandlerService,获取输入文件信息====={}",issue);
        // inputFile
        String filePath = super.getInputInfo(issue,productParam.getFileNamePattern(),productParam.getInputFilePath());
        inputXmlParam.setInputFile(filePath);

        //获取输入文件中的其他文件信息（其余两个配置到算法参数中）
        LinkedHashMap<String, Object> dynamicMap = triggerParam.getTaskParam().getDynamicMap();
        Map otherMap = new HashMap();
        String gribTmp = (String)dynamicMap.getOrDefault("grib_tmp", "/");
        String gribTmpRegex = (String)dynamicMap.getOrDefault("grib_tmp_regex", "Z_NWGD_C_BABJ_(yyyyMMdd).*._P_RFFC_SCMOC-TMP_(yyyyMMdd)2000_24003.GRB2$");

        otherMap.put("grib_tmp",super.getInputInfo(issue,gribTmpRegex,gribTmp));

        String gribPre = (String)dynamicMap.getOrDefault("grib_pre", "/");
        String gribPreRegex = (String)dynamicMap.getOrDefault("grib_pre_regex", "Z_NWGD_C_BABJ_(yyyyMMdd).*._P_RFFC_SCMOC-ER03_(yyyyMMdd)2000_24003.GRB2$");

        otherMap.put("grib_pre",super.getInputInfo(issue,gribPreRegex,gribPre));

        inputXmlParam.setOtherMap(otherMap);
        return null;
    }
}
