package com.htht.executor.fileStatistic.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.entity.dataReport.DataReportEntity;

import java.util.List;

/**
 * @author jiangjin
 * @email jiangjin@piesat.cn
 * @date 2022/9/26 15:59
 */
public interface DataReportService extends IService<DataReportEntity> {

    List<String> selectReportIssue(String id, String baseIssue);

    void updateRecordByIssueAndDataId(DataReportEntity dataReportEntity);

    /**
     * 集合添加数据
     * @param resultList
     */
    void saveAll(List<DataReportEntity> resultList);

    /**
     * 更新数据
     * @param updateReportList
     */
    void updateAll(List<DataReportEntity> updateReportList);

    List<DataReportEntity> selectReportEntity(String id, String substring, String cycle);
}
