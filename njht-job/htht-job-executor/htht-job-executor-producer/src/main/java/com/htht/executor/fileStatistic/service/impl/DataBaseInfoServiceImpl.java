package com.htht.executor.fileStatistic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.executor.fileStatistic.dao.DataBaseInfoDao;
import com.htht.executor.fileStatistic.service.DataBaseInfoService;
import com.htht.executor.statistics.param.StatisticsParam;
import com.njht.entity.dataReport.DataBaseInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jiangjin
 * @email jiangjin@piesat.cn
 * @date 2022/9/23 16:03
 */
@Service
public class DataBaseInfoServiceImpl extends ServiceImpl<DataBaseInfoDao, DataBaseInfoEntity> implements DataBaseInfoService {
    @Autowired
    private DataBaseInfoDao dataBaseInfoDao;

    @Override
    public List<String> selectBaseInfoId(String identify) {
        LambdaQueryWrapper<DataBaseInfoEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(DataBaseInfoEntity::getIdentify,identify);
        List<DataBaseInfoEntity> dataBaseInfoEntities = this.list(qw);
        return Optional.of(dataBaseInfoEntities.stream().map(DataBaseInfoEntity::getId).collect(Collectors.toList())).orElse(new ArrayList<>());    }

    @Override
    public List<DataBaseInfoEntity> selectBaseInfo(StatisticsParam statisticsParam) {
        List<String> identifyList = new ArrayList<>();
        LambdaQueryWrapper<DataBaseInfoEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(DataBaseInfoEntity::getIdentify,statisticsParam.getDataType());
        List<DataBaseInfoEntity> dataBaseInfoEntityList = Optional.ofNullable(this.list(qw)).orElse(new ArrayList<>());
        return dataBaseInfoEntityList;
    }

    @Override
    public DataBaseInfoEntity selectBaseInfoById(String id) {
        LambdaQueryWrapper<DataBaseInfoEntity> qw = new LambdaQueryWrapper<>();
        qw.in(DataBaseInfoEntity::getId,id);
        List<DataBaseInfoEntity> dataBaseInfoEntity = this.list(qw);
        return dataBaseInfoEntity.get(0);
    }

    @Override
    public void insertAll(List<DataBaseInfoEntity> dataBaseInfoEntityList) {
        dataBaseInfoDao.insertAll(dataBaseInfoEntityList);
    }
}
