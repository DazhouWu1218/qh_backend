package com.htht.executor.cimiss.service.impl;

import com.htht.executor.cimiss.bean.ResultBean;
import com.htht.executor.cimiss.service.CimissDownService;
import com.htht.executor.download.service.CimissDataDealService;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.entity.paramtemplate.CimissDownParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author daiguojun
 * @date 2022-08-09 10:09
 * 站点数据下载并入库
 */
@Slf4j
@Service("cimissSDKDownloadService")
public class CimissSDKDownloadServiceImpl extends CimissDownService {


    @Autowired
    private CimissDataDealService cimissDataDealService;

    @Override
    public void execute(CimissDownParam cimissParam,StringBuffer retStr) {

        String filePath = !Objects.isNull(cimissParam.getFilePath()) ? cimissParam.getFilePath() : "/home/htht/cimiss/CIMISS."+cimissParam.getDataFormat();
        log.info("--CimissHandler resultData save to " + filePath);
        try {
            FileUtils.writeStringToFile(new File(filePath), retStr.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("CimissHandler resultData "+ e.getMessage());
        }
        log.info("--CimissHandler---resultData--end");
        ResultBean resultBean = getFileOrStationData(retStr.toString());
        log.info("--CimissHandler---dealFileResult={}", Boolean.valueOf(resultBean.isFileResult()));
        if ("0".equals(resultBean.getReturnCode())) {
            cimissDataDealService.dealStationData(cimissParam, resultBean);
        }else {
            XxlJobHelper.handleFail("Cimiss数据下载失败");
        }
    }

    @Override
    public void setTimes(HashMap<String, String> map,String times) {
        map.put("times", times);
    }


}
