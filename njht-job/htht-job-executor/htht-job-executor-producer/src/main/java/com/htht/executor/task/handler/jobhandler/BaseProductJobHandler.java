package com.htht.executor.task.handler.jobhandler;

import com.htht.executor.product.service.ProductFileInfoService;
import com.htht.executor.task.service.BaseProductHandlerService;
import com.htht.executor.task.service.word.WordExecuteService;
import com.htht.executor.task.util.ProductServiceUtil;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.exception.CommonException;
import com.htht.job.core.handler.annotation.XxlJob;
import com.htht.job.core.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Slf4j
@Component
public class BaseProductJobHandler {

    @Autowired
    private ProductServiceUtil productServiceUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ProductFileInfoService productFileInfoService;

    /**
     * 产品调度handler
     */
    @XxlJob("baseProductHandler")
    public void baseProductHandler() throws Exception {
        TriggerParam triggerParam = XxlJobHelper.getTriggerParam();
        assert triggerParam != null;
        // 获取要执行的实现类
        TaskParam taskParam = triggerParam.getTaskParam();
        BaseProductHandlerService baseProductHandlerService = productServiceUtil.getServiceInfo(taskParam);
        // 执行具体handler业务逻辑
        ReturnT<String> result = baseProductHandlerService.execute(triggerParam);
        XxlJobHelper.handleResult(result.getCode(),result.getMsg());
    }


    @Value("${algorithm.exePath}")
    private String exePath;

    @Autowired
    private WordExecuteService wordExecuteService;

    /**
     * wordToPdf
     */
    @XxlJob("wordToPdfHandler")
    public void wordToPdfHandler() throws Exception {
        try {
            // 算法路劲
            LinkedHashMap<String, Object> dynamicMap = XxlJobHelper.getTriggerParam().getTaskParam().getDynamicMap();
            String exePath = (String)dynamicMap.getOrDefault("exePath", this.exePath);
            String executorParams = XxlJobHelper.getTriggerParam().getExecutorParams();
            XxlJobHelper.log("文件名称：{}",executorParams);
            wordExecuteService.wordToPdf(exePath,executorParams);
            XxlJobHelper.log(executorParams+">>>>>>>word2pdf>>>>success");
            XxlJobHelper.log("开始修改数据库 doc文件状态......");
            productFileInfoService.updateByFilePath(executorParams);
            XxlJobHelper.log("修改完成，转换完成........");
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(e.getMessage());
        }

    }

}
