package com.htht.executor.task.log;

import com.htht.executor.product.entity.JobLogStateEntity;
import com.htht.executor.product.service.JobLogStateService;
import com.htht.job.core.context.XxlJobContext;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

@Slf4j
@Component
public class ScheduleJobHelper extends XxlJobHelper {

    @Autowired
    private JobLogStateService jobLogStateService;

    private static ScheduleJobHelper scheduleJobHelper;

    @PostConstruct
    public void initJobLogService(){
        scheduleJobHelper = this;
        scheduleJobHelper.jobLogStateService = this.jobLogStateService;
    }

    /**
     * 保存日志
     * @param appendLogPattern
     * @param flag
     * @param appendLogArguments
     * @return
     */
    public static boolean logToFileAndDb(String appendLogPattern, boolean flag, Object ... appendLogArguments) {
        log.info(appendLogPattern,appendLogArguments);
        FormattingTuple ft = MessageFormatter.arrayFormat(appendLogPattern, appendLogArguments);
        String appendLog = ft.getMessage();
        StackTraceElement callInfo = new Throwable().getStackTrace()[1];
        return logDetail(callInfo, appendLog);
    }

    /**
     * 保存失败日志
     * @param handleMsg
     * @return
     */
    public static boolean handleFail(String handleMsg) {
        saveScheduleJobLog(handleMsg,2);
        return handleResult(XxlJobContext.HANDLE_COCE_FAIL, handleMsg);
    }

    /**
     * 保存异常信息
     *
     * @param e
     */
    public static boolean log(Throwable e) {

        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        String appendLog = stringWriter.toString();
        saveScheduleJobLog(appendLog,2);
        StackTraceElement callInfo = new Throwable().getStackTrace()[1];
        return logDetail(callInfo, appendLog);
    }

    /**
     * 保存调度日志到数据库中
     * @param msg
     * @param status
     */
    public static void saveScheduleJobLog(String msg, int status){
        JobLogStateEntity jobLogStateEntity = new JobLogStateEntity();
        jobLogStateEntity.setJobId(getJobId());
        jobLogStateEntity.setLogId(getTriggerParam().getLogId());
        jobLogStateEntity.setMsg(msg);
        jobLogStateEntity.setStatus(status);
        jobLogStateEntity.setCreateTime(new Date());
        jobLogStateEntity.setUpdateTime(new Date());
        jobLogStateEntity.setTime(System.currentTimeMillis());
        scheduleJobHelper.jobLogStateService.save(jobLogStateEntity);
    }

}
