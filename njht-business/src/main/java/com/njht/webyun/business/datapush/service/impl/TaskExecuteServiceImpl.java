package com.njht.webyun.business.datapush.service.impl;


import com.njht.entity.dataPush.TaskExecuteEntity;
import com.njht.entity.xxljob.XxlJobLogReport;
import com.njht.webyun.business.common.util.TimeUtil;
import com.njht.webyun.business.datapush.constant.EsConstant;
import com.njht.webyun.business.datapush.service.DataPushService;
import com.njht.webyun.business.feign.AdminFeignService;
import com.njht.webyun.business.index.constant.IndexConstant;
import com.njht.webyun.business.index.vo.TimeParam;
import com.njht.webyun.enums.IdentifyTypeEnum;
import com.njht.webyun.utils.DateFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author daiguojun
 * @date 2022-09-06 15:25
 * 任务执行情况统计 实现类
 */
@Service(EsConstant.TASK_EXECUTE)
@Slf4j
public class TaskExecuteServiceImpl implements DataPushService {

    @Autowired
    private AdminFeignService adminFeignService;

    @Override
    public List<?> execute() {
        log.info("任务执行情况统计");
        // 获取当天任务执行情况
        return this.queryLogReport();
    }

    /**
     * 处理参数并返回
     * @return
     */
    private List<TaskExecuteEntity> queryLogReport() {
        // 获取开始结束时间
        TimeParam timeInfo = TimeUtil.getTimeInfo(IndexConstant.INDEX_ONE);
        String beginTime = DateFormatUtils.dateToStr(timeInfo.getBeginDate(), DateFormatUtils.formatYY_MM_dd_ss);
        String endTime = DateFormatUtils.dateToStr(timeInfo.getEndDate(), DateFormatUtils.formatYY_MM_dd_ss);
        List<XxlJobLogReport> dbList = adminFeignService.logReportList(beginTime, endTime).getData();
        return Optional.ofNullable(dbList).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    TaskExecuteEntity req = new TaskExecuteEntity();
                    req.setTaskType(IdentifyTypeEnum.getValue(item.getTreeKey()));
                    req.setExeFailedCount(item.getFailCount());
                    req.setExeSuccessCount(item.getSucCount());
                    req.setExeBeingCount(item.getRunningCount());
                    req.setRecordTime(item.getTriggerDay());
                    int count = req.getExeBeingCount() + req.getExeSuccessCount() + req.getExeFailedCount();
                    req.setExeFailed(getPercent(item.getFailCount(),count));
                    req.setExeBeing(getPercent(item.getRunningCount(),count));
                    req.setExeSuccess(getPercent(item.getSucCount(),count));
                    return req;
                }).collect(Collectors.toList());
    }

    /**
     * 获取 任务执行 百分比
     * @param failCount
     * @param count
     * @return
     */
    private static String getPercent(int failCount, int count) {
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        percentInstance.setMinimumFractionDigits(2);
        percentInstance.setRoundingMode(RoundingMode.FLOOR);
        if (count == 0) {
            return percentInstance.format(0);
        }else {
            return percentInstance.format((double) failCount / (double)(count));
        }
    }
}
