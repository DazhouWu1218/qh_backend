package com.htht.job.admin.dispatch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.job.admin.dispatch.core.model.XxlJobLog;
import com.htht.job.admin.dispatch.vo.JobLogVo;
import com.htht.job.admin.dispatch.vo.LogTypeEntity;
import com.htht.job.core.biz.model.LogResult;
import com.htht.job.core.biz.model.ReturnT;
import com.njht.entity.xxljob.CountInfo;
import com.njht.entity.xxljob.XxlJobLogReport;
import com.njht.webyun.utils.PageUtils;

import java.util.Date;
import java.util.List;

/**
 * 任务日志表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-06-23 17:44:45
 */
public interface JobLogService extends IService<XxlJobLog> {
    /**
     * 分页查询日志
     * @param jobLogVo
     * @param productIds
     * @return
     */
    PageUtils queryPage(JobLogVo jobLogVo, List<String> productIds);

    /**
     * 日志清理
     * @param treeId
     * @param jobGroup
     * @param jobId
     * @param type
     */
    void clearLog(String treeId, int jobGroup, int jobId, int type);

    /**
     * 查询日志详情
     * @param executorAddress
     * @param time
     * @param logId
     * @param fromLineNum
     * @return
     */
    ReturnT<LogResult> queryLogDetail(String executorAddress, long time, long logId, int fromLineNum);

    /**
     * 终止正在运行的任务
     * @param id
     */
    ReturnT<String> logKill(int id);

    /**
     * 删除关系对照列表
     * @return
     */
    List<LogTypeEntity> typeList();

    /**
     * 日志结果统计
     * @param todayFrom
     * @param todayTo
     */
    void statisticLog(Date todayFrom, Date todayTo);

    /**
     * 查询日志成功率
     * @param beginTime
     * @param endTime
     * @return
     */
    List<XxlJobLogReport> logReportList(String beginTime, String endTime);

    /**
     * 通过id 删除
     * @param logId
     */
    void deleteById(Long logId);

    /**
     * 统计log，task，group count
     * @return
     */
    CountInfo countInfo();
}

