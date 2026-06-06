package com.njht.webyun.business.datapush.service.impl;


import com.njht.entity.dataPush.DataStatusEntity;
import com.njht.entity.dataReport.DataReportEntity;
import com.njht.webyun.business.datapush.constant.EsConstant;
import com.njht.webyun.business.datapush.service.DataPushService;
import com.njht.webyun.business.report.enums.ReportEnum;
import com.njht.webyun.business.report.service.DataReportService;
import com.njht.webyun.business.report.service.impl.DataReportServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author daiguojun
 * @date 2022-09-06 15:25
 * 数据到达情况统计 实现类
 */
@Service(EsConstant.DATA_COLLECT)
@Slf4j
public class DataPushServiceImpl implements DataPushService {


    @Autowired
    private DataReportService dataReportService;

    @Override
    public List<?> execute() {
        log.info("数据到达情况统计");
        // 获取当天数据到达情况
        return this.queryDataStatusList(dataReportService.queryDataList());

    }

    /**
     * 处理参数并返回
     * @param dbList
     * @return
     */
    private List<DataStatusEntity> queryDataStatusList(List<DataReportEntity> dbList) {
        return Optional.ofNullable(dbList).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    DataStatusEntity dataStatusEntity = new DataStatusEntity();
                    dataStatusEntity.setDataType(item.getDataName());
                    dataStatusEntity.setDataCycle(item.getIssue());
                    dataStatusEntity.setDataSize(Double.valueOf(DataReportServiceImpl.getFileSize(item.getFileSize())));
                    dataStatusEntity.setDataNumber(item.getFileNum().intValue());
                    String rate = String.format("%.5f", (double) item.getFileNum() / (double) item.getSumNum());
                    dataStatusEntity.setDataArrivalRate(rate);
                    dataStatusEntity.setDataIntegrity(rate);
                    dataStatusEntity.setDataProduction(ReportEnum.getDesc(item.getStatus()));
                    return dataStatusEntity;
                }).sorted(Comparator.comparing(DataStatusEntity::getDataType).thenComparing(DataStatusEntity::getDataCycle))
                .collect(Collectors.toList());
    }

}
