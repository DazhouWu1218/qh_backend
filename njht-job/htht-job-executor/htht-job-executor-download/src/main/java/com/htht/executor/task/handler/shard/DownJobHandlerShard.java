package com.htht.executor.task.handler.shard;

import com.alibaba.fastjson.JSON;
import com.htht.executor.task.service.BaseDownJobService;
import com.htht.executor.task.strategy.DownJobShardContext;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.entity.paramtemplate.DownParam;
import com.htht.job.core.exception.CommonException;
import com.htht.job.core.shard.SharingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zsz
 */
@Service("downJobHandlerShard")
@Slf4j
public class DownJobHandlerShard implements SharingHandler {

    @Autowired
    private DownJobShardContext downJobShardContext;

    @Override
    public ReturnT<List<String>> executeShard(TaskParam taskParam) {
        log.info("exec download job handler shard");
        DownParam downParam = JSON.parseObject(taskParam.getModelParameters(), DownParam.class);
        if (downParam == null) {
            throw new CommonException("模板参数获取失败");
        }
        BaseDownJobService baseDownJobService = downJobShardContext.getDownJobShardByType(downParam.getForSouceType());
        // 执行分片任务
        return baseDownJobService.executeShard(taskParam);
    }
}
