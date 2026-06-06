package com.htht.job.admin.dispatch.core.route.strategy;

import com.htht.job.admin.dispatch.core.route.ExecutorRouter;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TriggerParam;

import java.util.List;

/**
 * 最后一个节点执行
 * Created by piesat on 17/3/10.
 */
public class ExecutorRouteLast extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        return new ReturnT<>(addressList.get(addressList.size()-1));
    }

}
