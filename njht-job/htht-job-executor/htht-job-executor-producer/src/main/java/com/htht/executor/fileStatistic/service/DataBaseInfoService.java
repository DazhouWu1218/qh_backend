package com.htht.executor.fileStatistic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.executor.statistics.param.StatisticsParam;
import com.njht.entity.dataReport.DataBaseInfoEntity;

import java.util.List;

/**
 * @author jiangjin
 * @email jiangjin@piesat.cn
 * @date 2022/9/23 16:00
 */
public interface DataBaseInfoService extends IService<DataBaseInfoEntity> {
    /**
     * 查询identify参数配置的ID
     *
     * @param identify
     * @return
     */
    List<String> selectBaseInfoId(String identify);


    /**
     * 查询原始和预处理的参数配置
     * @param statisticsParam
     * @return
     */
    List<DataBaseInfoEntity> selectBaseInfo(StatisticsParam statisticsParam);

    /**
     * 根据id查询基本参数配置表
     *
     * @param id
     * @return
     */
    DataBaseInfoEntity selectBaseInfoById(String id);

    /**
     * 批量插入产品配置信息
     * @param dataBaseInfoEntityList
     */
    void insertAll(List<DataBaseInfoEntity> dataBaseInfoEntityList);

}
