package com.njht.webyun.management.dataanalysis.systemdatacount.schedule;

import com.njht.webyun.management.dataanalysis.systemdatacount.service.LogSystemDataCountDiskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 接收发布数据定时扫描磁盘统计入库
 *
 * @author zhouhouliang
 * @date 2021/7/14 20:09
 */
@Slf4j
@Component
public class LogSystemDataCountCalculateSchedule {

    @Autowired
    LogSystemDataCountDiskService logSystemDataCountDiskService;

    /**
     * TODO 本方案存在严重性能问题
     * 定时去计算系统中个类别数据接收的总量
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void calculateDataReceive() {
        logSystemDataCountDiskService.calculateDataReceive();
    }

}
