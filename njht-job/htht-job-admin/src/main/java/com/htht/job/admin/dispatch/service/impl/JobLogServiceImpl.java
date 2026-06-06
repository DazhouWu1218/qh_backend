package com.htht.job.admin.dispatch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.htht.job.admin.common.ThreadLocalParams;
import com.htht.job.admin.dispatch.core.complete.XxlJobCompleter;
import com.htht.job.admin.dispatch.core.model.XxlJobInfo;
import com.htht.job.admin.dispatch.core.model.XxlJobLog;
import com.htht.job.admin.dispatch.core.scheduler.XxlJobScheduler;
import com.htht.job.admin.dispatch.core.util.I18nUtil;
import com.htht.job.admin.dispatch.dao.XxlJobGroupDao;
import com.htht.job.admin.dispatch.dao.XxlJobInfoDao;
import com.htht.job.admin.dispatch.dao.XxlJobLogDao;
import com.htht.job.admin.dispatch.dao.XxlJobLogReportDao;
import com.htht.job.admin.dispatch.service.DataCategoryService;
import com.htht.job.admin.dispatch.service.JobLogService;
import com.htht.job.admin.dispatch.util.LogTypeEnum;
import com.htht.job.admin.dispatch.vo.JobLogReqVo;
import com.htht.job.admin.dispatch.vo.JobLogVo;
import com.htht.job.admin.dispatch.vo.LogTypeEntity;
import com.htht.job.core.biz.ExecutorBiz;
import com.htht.job.core.biz.model.KillParam;
import com.htht.job.core.biz.model.LogParam;
import com.htht.job.core.biz.model.LogResult;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.constant.DateConstant;
import com.htht.job.core.exception.CommonException;
import com.htht.job.core.util.DateUtil;
import com.njht.entity.xxljob.CountInfo;
import com.njht.entity.xxljob.XxlJobLogReport;
import com.njht.webyun.utils.PageUtil;
import com.njht.webyun.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务日志表
 * @author daiguojun
 * @date 2022-06-23 17:44:45
 */
@Service("jobLogService")
@Slf4j
public class JobLogServiceImpl extends ServiceImpl<XxlJobLogDao, XxlJobLog> implements JobLogService {

    @Resource
    public XxlJobInfoDao xxlJobInfoDao;
    @Resource
    public XxlJobLogDao xxlJobLogDao;

    @Resource
    private XxlJobLogReportDao xxlJobLogReportDao;
    @Resource
    private XxlJobGroupDao xxlJobGroupDao;

    @Override
    public PageUtils queryPage(JobLogVo jobLogVo, List<String> productIds) {
        PageUtil.setPageAndSize(jobLogVo.getPage(),jobLogVo.getSize(),1,10);
        List<XxlJobLog> jobLogList = baseMapper.page(productIds,jobLogVo.getJobGroup(), jobLogVo.getJobId(), jobLogVo.getTriggerTimeStart(), jobLogVo.getTriggerTimeEnd(), jobLogVo.getLogStatus());
        PageUtils pageUtils = new PageUtils(new PageInfo<>(jobLogList));
        // 根据任务id查询任务名称
        Set<Integer> jobIdList = Optional.ofNullable(jobLogList).orElse(new ArrayList<>())
                .stream()
                .map(XxlJobLog::getJobId).collect(Collectors.toSet());

        List<XxlJobInfo> jobInfos = new ArrayList<>();
        if (!jobIdList.isEmpty()) {
            jobInfos = xxlJobInfoDao.getJobInfoByJobIds(jobIdList);
        }
        List<XxlJobInfo> finalJobInfos = jobInfos;
        List<JobLogReqVo> collect = Optional.ofNullable(jobLogList).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    JobLogReqVo reqVo = new JobLogReqVo();
                    BeanUtils.copyProperties(item, reqVo);
                    String jobDesc = finalJobInfos.stream().filter(jobInfo -> jobInfo.getId() == reqVo.getJobId()).findAny().orElse(new XxlJobInfo()).getJobDesc();
                    reqVo.setJobName(jobDesc);
                    return reqVo;
                }).collect(Collectors.toList());
        pageUtils.setList(collect);
        return pageUtils;
    }

    @Override
    public void clearLog(String treeId, int jobGroup, int jobId, int type) {
        List<String> productIds = ThreadLocalParams.productIds.get();
        Date clearBeforeTime = null;
        int clearBeforeNum = 0;
        if (type == 1) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -1);	// 清理一个月之前日志数据
        } else if (type == 2) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -3);	// 清理三个月之前日志数据
        } else if (type == 3) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -6);	// 清理六个月之前日志数据
        } else if (type == 4) {
            clearBeforeTime = DateUtil.addYears(new Date(), -1);	// 清理一年之前日志数据
        } else if (type == 5) {
            clearBeforeNum = 1000;		// 清理一千条以前日志数据
        } else if (type == 6) {
            clearBeforeNum = 10000;		// 清理一万条以前日志数据
        } else if (type == 7) {
            clearBeforeNum = 30000;		// 清理三万条以前日志数据
        } else if (type == 8) {
            clearBeforeNum = 100000;	// 清理十万条以前日志数据
        } else if (type == 9) {
            clearBeforeNum = 0;			// 清理所有日志数据
        } else {
            throw new CommonException(I18nUtil.getString("joblog_clean_type_unvalid"));
        }
        List<Long> logIds = null;
        do {
            logIds = xxlJobLogDao.findClearLogIds(productIds,jobGroup, jobId, clearBeforeTime, clearBeforeNum, 1000);
            if (logIds!=null && !logIds.isEmpty()) {
                xxlJobLogDao.clearLog(logIds);
            }
        } while (logIds!=null && !logIds.isEmpty());

    }

    @Override
    public ReturnT<LogResult> queryLogDetail(String executorAddress, long time, long logId, int fromLineNum) {
        try {
            ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(executorAddress);
            ReturnT<LogResult> logResult = executorBiz.log(new LogParam(time, logId, fromLineNum));

            // is end
            if (logResult.getData()!=null && logResult.getData().getFromLineNum() > logResult.getData().getToLineNum()) {
                XxlJobLog jobLog = xxlJobLogDao.load(logId);
                if (jobLog.getHandleCode() > 0) {
                    logResult.getData().setEnd(true);
                }
            }

            return logResult;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ReturnT<>(ReturnT.FAIL_CODE, e.getMessage());
        }
    }

    @Override
    public ReturnT<String> logKill(int id) {
        XxlJobLog jobLog = xxlJobLogDao.load(id);
        XxlJobInfo jobInfo = xxlJobInfoDao.loadById(jobLog.getJobId());
        if (jobInfo==null) {
            throw new CommonException(I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
        }
        if (ReturnT.SUCCESS_CODE != jobLog.getTriggerCode()) {
            throw new CommonException(I18nUtil.getString("joblog_kill_log_limit"));
        }
        // request of kill
        ReturnT<String> runResult = null;
        try {
            ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(jobLog.getExecutorAddress());
            runResult = executorBiz.kill(new KillParam(jobInfo.getId()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }

        if (ReturnT.SUCCESS_CODE == runResult.getCode()) {
            // 日志成功终止,修改任务执行状态为执行失败（任务被终止，所以执行失败）
            jobLog.setHandleCode(ReturnT.FAIL_CODE);
            jobLog.setHandleMsg( I18nUtil.getString("joblog_kill_log_byman")+":" + (runResult.getMsg()!=null?runResult.getMsg():""));
            jobLog.setHandleTime(new Date());
            XxlJobCompleter.updateHandleInfoAndFinish(jobLog);
            return ReturnT.success(runResult.getMsg());
        } else {
            return new ReturnT<>(500, runResult.getMsg());
        }
    }

    @Override
    public List<LogTypeEntity> typeList() {
        List<LogTypeEntity> logEnumList = new ArrayList<>();
        // 日志删除类型对照关系
        for (LogTypeEnum st : LogTypeEnum.values()) {
            LogTypeEntity entity = new LogTypeEntity(st.getKey(),st.getValue());
            logEnumList.add(entity);
        }
        return logEnumList;
    }

    @Autowired
    private DataCategoryService dataCategoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void statisticLog(Date todayFrom, Date todayTo) {
        // 获取数据目录标识 以及对应treeId
        Map<String,List<String>> treeKeyMap = dataCategoryService.getTreeKeyMap();
        // 不同标识统计结果入库
        Optional.ofNullable(treeKeyMap).orElse(new HashMap<>(3))
                .forEach((key,value) -> this.saveLogReportInfo(todayFrom,todayTo,key,value));
    }

    @Override
    public List<XxlJobLogReport> logReportList(String beginTime, String endTime) {
        Date beginDate = DateUtil.strToDate(beginTime, DateConstant.YYYY_MM_DD_HH_MM_SS);
        Date endDate = DateUtil.strToDate(endTime, DateConstant.YYYY_MM_DD_HH_MM_SS);
        return xxlJobLogReportDao.queryLogReport(beginDate, endDate);
    }

    @Override
    public void deleteById(Long logId) {
        xxlJobLogDao.clearLog(Arrays.asList(logId));
    }

    @Override
    public CountInfo countInfo() {
        CountInfo countInfo = new CountInfo();
        // 调度总数
        countInfo.setDispatchCount(this.count());
        // 任务总数
        countInfo.setTasCount(xxlJobInfoDao.findAllCount());
        // 执行器总数
        countInfo.setGroupCount(xxlJobGroupDao.findAllCount());
        return countInfo;
    }




    /**
     * 统不同类型的任务调度成功率
     * @param todayFrom
     * @param todayTo
     * @param identify
     * @param treeIdList
     */
    private void saveLogReportInfo(Date todayFrom, Date todayTo,String identify, List<String> treeIdList) {
        XxlJobLogReport xxlJobLogReport = new XxlJobLogReport();
        xxlJobLogReport.setTriggerDay(todayFrom);
        Map<String, Object> triggerCountMap = xxlJobLogDao.findLogReport(todayFrom, todayTo,treeIdList);
        if (triggerCountMap!=null && triggerCountMap.size()>0) {
            int triggerDayCount = triggerCountMap.containsKey("triggerDayCount")?Integer.parseInt(String.valueOf(triggerCountMap.get("triggerDayCount"))):0;
            int triggerDayCountRunning = triggerCountMap.containsKey("triggerDayCountRunning")?Integer.parseInt(String.valueOf(triggerCountMap.get("triggerDayCountRunning"))):0;
            int triggerDayCountSuc = triggerCountMap.containsKey("triggerDayCountSuc")?Integer.parseInt(String.valueOf(triggerCountMap.get("triggerDayCountSuc"))):0;
            int triggerDayCountFail = triggerDayCount - triggerDayCountRunning - triggerDayCountSuc;

            xxlJobLogReport.setRunningCount(triggerDayCountRunning);
            xxlJobLogReport.setSucCount(triggerDayCountSuc);
            xxlJobLogReport.setFailCount(triggerDayCountFail);
            xxlJobLogReport.setTreeKey(identify);
        }

        // do refresh
        int ret = xxlJobLogReportDao.update(xxlJobLogReport);
        if (ret < 1) {
            xxlJobLogReportDao.save(xxlJobLogReport);
        }
    }

}