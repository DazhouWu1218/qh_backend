package com.htht.executor.fileStatistic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.executor.fileStatistic.dao.DataReportDao;
import com.htht.executor.fileStatistic.service.DataReportService;
import com.njht.entity.dataReport.DataReportEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jiangjin
 * @email jiangjin@piesat.cn
 * @date 2022/9/26 16:59
 */
@Service
public class DataReportServiceImpl extends ServiceImpl<DataReportDao, DataReportEntity> implements DataReportService {

    @Autowired
    private DataReportDao dataReportDao;

    @Override
    public List<String> selectReportIssue(String id, String baseIssue) {
        LambdaQueryWrapper<DataReportEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(DataReportEntity::getDataId, id);
        qw.likeRight(!StringUtils.isEmpty(baseIssue), DataReportEntity::getIssue, baseIssue);
        List<DataReportEntity> dataReportEntityList = this.list(qw);
        return Optional.of(dataReportEntityList.stream().map(DataReportEntity::getIssue).collect(Collectors.toList())).orElse(new ArrayList<>());
    }

    @Override
    public void updateRecordByIssueAndDataId(DataReportEntity dataReportEntity) {
        LambdaUpdateWrapper<DataReportEntity> qw = new LambdaUpdateWrapper<>();
        qw.eq(DataReportEntity::getIssue,dataReportEntity.getIssue()).
                eq(DataReportEntity::getDataId,dataReportEntity.getDataId());
        qw.set(DataReportEntity::getFileNum,dataReportEntity.getFileNum())
                .set(DataReportEntity::getFileSize,dataReportEntity.getFileSize())
                .set(DataReportEntity::getStatus,dataReportEntity.getStatus());
        this.update(null, qw);
    }

    @Override
    public void saveAll(List<DataReportEntity> dataList) {
        dataReportDao.insertList(dataList);
    }

    @Override
    public void updateAll(List<DataReportEntity> updateReportList) {
        baseMapper.updateList(updateReportList);
    }

    @Override
    public List<DataReportEntity> selectReportEntity(String id, String baseIssue, String cycle) {
        LambdaQueryWrapper<DataReportEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(DataReportEntity::getDataId, id);
        qw.eq(DataReportEntity::getCycle, cycle);
        qw.likeRight(!StringUtils.isEmpty(baseIssue), DataReportEntity::getIssue, baseIssue);
        return this.list(qw);
    }
}
