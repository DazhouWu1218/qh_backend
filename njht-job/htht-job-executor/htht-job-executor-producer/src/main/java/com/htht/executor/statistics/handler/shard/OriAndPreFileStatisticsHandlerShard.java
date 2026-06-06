package com.htht.executor.statistics.handler.shard;

import com.alibaba.fastjson.JSON;
import com.htht.executor.statistics.base.FileStatisticsJobService;
import com.htht.executor.statistics.param.StatisticsParam;
import com.htht.executor.statistics.strategy.OriAndPreFileStatisticsContext;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.shard.SharingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jiangjin
 * @email jiangjin@piesat.cn
 * 原始文件和预处理文件统计分片
 * @date 2022/9/23 14:54
 */
@Service("oriAndPreFileStatisticsHandlerShard")
@Slf4j
public class OriAndPreFileStatisticsHandlerShard implements SharingHandler {

    @Autowired
    private OriAndPreFileStatisticsContext oriAndPreFileStatisticsContext;


    @Override
    public ReturnT<List<String>> executeShard(TaskParam taskParam) {
        log.info("exec statistics job handler shard");
        // 页面模板参数
        String params = taskParam.getModelParameters();
        StatisticsParam statisticsParam = JSON.parseObject(params, StatisticsParam.class);
        FileStatisticsJobService fileStatisticsJobService = oriAndPreFileStatisticsContext.getStatisticsShardByType(statisticsParam.getDataType());
        // 执行统计任务的shard
        return fileStatisticsJobService.executeShard(taskParam);
    }
}
