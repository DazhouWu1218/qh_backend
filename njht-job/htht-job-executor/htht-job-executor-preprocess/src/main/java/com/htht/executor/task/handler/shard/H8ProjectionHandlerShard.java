package com.htht.executor.task.handler.shard;

import com.htht.executor.task.service.h8.H8PreprocessService;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.shard.SharingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 代国军
 * @description: h8预处理分片
 * @date 2022/5/24 16:09
 */
@Service("h8ProjectionHandlerShard")
@Slf4j
public class H8ProjectionHandlerShard implements SharingHandler {

    @Autowired
    private H8PreprocessService h8PreprocessService;

    @Override
    public ReturnT<List<String>> executeShard(TaskParam taskParam) {
        log.info("exec H8 preprocess handler shard");
        return h8PreprocessService.executeShard(taskParam);
    }
}
