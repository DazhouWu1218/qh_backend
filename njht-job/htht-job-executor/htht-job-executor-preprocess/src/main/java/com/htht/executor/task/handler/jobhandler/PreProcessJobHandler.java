package com.htht.executor.task.handler.jobhandler;

import com.htht.executor.task.service.base.PreJobService;
import com.htht.executor.task.service.h8.H8PreprocessService;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 代国军
 * @description: 预处理任务handler
 * @date 2022/5/19 13:35
 */
@Component
@Slf4j
public class PreProcessJobHandler {


    @Autowired
    private H8PreprocessService h8PreprocessService;

    @Autowired
    private PreJobService preJobService;

    /**
     * 预处理任务，smart数据预处理
     * @throws Exception
     */
    @XxlJob("satelliteDataHandler")
    public void satelliteDataHandler() throws Exception {
        log.info("exec satellite date preprocess handler");
        TriggerParam triggerParam = XxlJobHelper.getTriggerParam();
        preJobService.execute(triggerParam);
    }

    @XxlJob("h8ProjectionHandler")
    public void h8ProjectionHandler() throws Exception {
        log.info("exec H8 preprocess handler");
        h8PreprocessService.execute(XxlJobHelper.getTriggerParam());
    }

}
