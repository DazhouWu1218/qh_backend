package com.htht.executor.task.service.base;


import com.htht.job.core.biz.model.TriggerParam;

/**
 * @author 代国军
 * @description: 预处理任务
 * @date 2022/5/19 15:51
 */
public interface PreJobService {

    /**
     * handler 执行
     * @param triggerParam
     */
    void execute(TriggerParam triggerParam);


    /**
     * 获取执行文件信息
     * @param jobParam
     * @param triggerParam
     * @return
     */
    String getExecuteFile(String jobParam, TriggerParam triggerParam);


    /**
     * 获取执行期次信息
     * @param jobParam
     * @param triggerParam
     * @return
     */
    String getExecuteIssue(String jobParam, TriggerParam triggerParam);
}
