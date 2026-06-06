package com.htht.job.core.biz;

import com.htht.job.core.biz.model.*;
import com.htht.job.core.biz.model.*;

import java.util.List;

/**
 * Created by piesat on 17/3/1.
 */
public interface ExecutorBiz {

    /**
     * beat
     * @return
     */
    public ReturnT<String> beat();

    /**
     * idle beat
     *
     * @param idleBeatParam
     * @return
     */
    public ReturnT<String> idleBeat(IdleBeatParam idleBeatParam);

    /**
     * run
     * @param triggerParam
     * @return
     */
    public ReturnT<String> run(TriggerParam triggerParam);

    /**
     * kill
     * @param killParam
     * @return
     */
    public ReturnT<String> kill(KillParam killParam);

    /**
     * log
     * @param logParam
     * @return
     */
    public ReturnT<LogResult> log(LogParam logParam);

    /**
     * 分片调度
     * @param triggerParam
     * @return
     */
    public ReturnT<List<String>> runShard(TriggerParam triggerParam);
}
