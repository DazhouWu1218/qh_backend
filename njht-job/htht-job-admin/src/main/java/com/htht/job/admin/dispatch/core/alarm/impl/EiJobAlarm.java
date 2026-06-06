package com.htht.job.admin.dispatch.core.alarm.impl;

import com.htht.job.admin.dispatch.core.alarm.JobAlarm;
import com.htht.job.admin.dispatch.core.model.XxlJobInfo;
import com.htht.job.admin.dispatch.core.model.XxlJobLog;
import com.htht.job.admin.feign.BusinessFeignService;
import com.htht.job.admin.feign.DataCenterFeignService;
import com.njht.entity.category.DataCategoryEntity;
import com.njht.entity.dataPush.SingleTaskExeEntity;
import com.njht.webyun.enums.IdentifyTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * job alarm by 天境
 *
 * @author piesat 2020-01-19
 */
@Component
@Slf4j
public class EiJobAlarm implements JobAlarm {

    @Autowired
    private DataCenterFeignService dataCenterFeignService;

    @Autowired
    private BusinessFeignService businessFeignService;

    /**
     * fail alarm
     *
     * @param jobLog
     */
    @Override
    public boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog){
        boolean alarmResult = true;
        // send monitor email
        if (info!=null && info.getAlarmEmail()!=null && info.getAlarmEmail().trim().length()>0) {


            List<DataCategoryEntity> dbList = dataCenterFeignService.treeList().getData();
            // 获取数据标识
            String taskType = Optional.ofNullable(dbList).orElse(new ArrayList<>())
                    .stream()
                    .filter(dataCategoryEntity -> dataCategoryEntity.getId().equals(jobLog.getTreeId()))
                    .findFirst().orElse(new DataCategoryEntity())
                    .getTreeKey();
            // 封装失败参数信息
            SingleTaskExeEntity singleTaskExeEntity = new SingleTaskExeEntity(IdentifyTypeEnum.getValue(taskType),
                    String.valueOf(info.getId()),info.getJobDesc(),"失败",jobLog.getHandleMsg());
            try {
                // 通过监控中心 告警失败信息
                alarmResult = businessFeignService.logAlarm(singleTaskExeEntity).getData();
            } catch (Exception e) {
                e.printStackTrace();
                alarmResult = false;
            }

        }

        return alarmResult;
    }


}
