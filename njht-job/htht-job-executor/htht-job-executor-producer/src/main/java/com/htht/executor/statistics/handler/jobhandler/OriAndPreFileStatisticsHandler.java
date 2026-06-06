package com.htht.executor.statistics.handler.jobhandler;

import com.alibaba.fastjson.JSON;
import com.htht.executor.statistics.base.FileStatisticsJobService;
import com.htht.executor.statistics.base.ProductBaseCfgToDbService;
import com.htht.executor.statistics.param.StatisticsParam;
import com.htht.executor.statistics.strategy.OriAndPreFileStatisticsContext;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author zhushizhen
 * @Date 2022-09-27 10:36
 **/
@Slf4j
@Component
public class OriAndPreFileStatisticsHandler {

    @Autowired
    private OriAndPreFileStatisticsContext oriAndPreFileStatisticsContext;

    @Autowired
    private ProductBaseCfgToDbService productBaseCfgToDbService;

    /**
     * product配置信息入库BASE表中
     * @throws Exception
     */
    @XxlJob("ProductBaseCfgToDbHandler")
    public void productBaseCfgToDb() throws Exception {
        log.info("exec ProductBaseCfgToDb job handler");
        TriggerParam triggerParam = XxlJobHelper.getTriggerParam();
        assert triggerParam != null;
        // 执行具体handler业务逻辑
        ReturnT<String> result = productBaseCfgToDbService.execute(triggerParam);
        XxlJobHelper.handleResult(result.getCode(),result.getMsg());
    }

    /**
     * 数据下载 预处理监控handler
     */
    @XxlJob("oriAndPreFileStatisticsHandler")
    public void oriAndPreFileStatisticsHandler() throws Exception {
        log.info("exec statistics job handler");
        TriggerParam triggerParam = XxlJobHelper.getTriggerParam();
        assert triggerParam != null;
        // 获取要执行的实现类
        TaskParam taskParam = triggerParam.getTaskParam();
        // 页面模板参数
        String params = taskParam.getModelParameters();
        StatisticsParam statisticsParam = JSON.parseObject(params, StatisticsParam.class);
        FileStatisticsJobService fileStatisticsJobService = oriAndPreFileStatisticsContext.getStatisticsShardByType(statisticsParam.getDataType());
        // 执行具体handler业务逻辑
        ReturnT<String> result = fileStatisticsJobService.execute(triggerParam);
        XxlJobHelper.handleResult(result.getCode(),result.getMsg());
    }
}
