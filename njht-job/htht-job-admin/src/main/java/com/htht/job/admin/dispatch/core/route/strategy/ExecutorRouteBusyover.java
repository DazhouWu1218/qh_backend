package com.htht.job.admin.dispatch.core.route.strategy;

import com.htht.job.admin.dispatch.core.scheduler.XxlJobScheduler;
import com.htht.job.admin.dispatch.core.route.ExecutorRouter;
import com.htht.job.admin.dispatch.core.util.I18nUtil;
import com.htht.job.core.biz.ExecutorBiz;
import com.htht.job.core.biz.model.IdleBeatParam;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TriggerParam;

import java.util.List;

/**
 * 忙碌转移
 * for循环判断每个节点的该任务线程 是否有排队任务或者正在运行，有排队任务返回失败，继续判断下一个节点
 * 需要调度中心去调度每个执行器节点（远程调用）
 * Created by piesat on 17/3/10.
 */
public class ExecutorRouteBusyover extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        StringBuffer idleBeatResultSB = new StringBuffer();
        for (String address : addressList) {
            // beat
            ReturnT<String> idleBeatResult = null;
            try {
                ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(address);
                idleBeatResult = executorBiz.idleBeat(new IdleBeatParam(triggerParam.getJobId()));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                idleBeatResult = new ReturnT<>(ReturnT.FAIL_CODE, ""+e );
            }
            idleBeatResultSB.append( (idleBeatResultSB.length()>0)?"<br><br>":"")
                    .append(I18nUtil.getString("jobconf_idleBeat") + "：")
                    .append("<br>address：").append(address)
                    .append("<br>code：").append(idleBeatResult.getCode())
                    .append("<br>msg：").append(idleBeatResult.getMsg());

            // beat success
            if (idleBeatResult.getCode() == ReturnT.SUCCESS_CODE) {
                idleBeatResult.setMsg(idleBeatResultSB.toString());
                idleBeatResult.setData(address);
                return idleBeatResult;
            }
        }

        return new ReturnT<>(ReturnT.FAIL_CODE, idleBeatResultSB.toString());
    }

}
