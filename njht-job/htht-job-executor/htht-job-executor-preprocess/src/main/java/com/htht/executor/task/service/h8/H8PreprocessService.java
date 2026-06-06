package com.htht.executor.task.service.h8;

import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TriggerParam;

import java.util.List;

public interface H8PreprocessService {

    /**
     * 执行H8 预处理任务
     * @param triggerParam
     */
    void execute(TriggerParam triggerParam);

    /**
     * h8分片
     * @param taskParam
     * @return
     */
    ReturnT<List<String>> executeShard(TaskParam taskParam);
}
