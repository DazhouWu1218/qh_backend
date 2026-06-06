package com.njht.webyun.business.report.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.entity.dataReport.DataBaseInfoEntity;
import com.njht.entity.dataReport.DataReportEntity;
import com.njht.webyun.business.report.constant.ReportConstant;
import com.njht.webyun.business.report.dao.DataBaseInfoDao;
import com.njht.webyun.business.report.service.DataBaseInfoService;
import com.njht.webyun.business.report.service.DataReportService;
import com.njht.webyun.business.report.vo.DataReportReqVo;
import com.njht.webyun.constant.DbConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 数据监控基础信息
 * @author daiguojun
 * @date 2022-08-16 15:49:24
 */
@Service("dataBaseInfoService")
@DS(value = DbConstant.MYSQL_1)
public class DataBaseInfoServiceImpl extends ServiceImpl<DataBaseInfoDao, DataBaseInfoEntity> implements DataBaseInfoService {

    @Autowired
    private DataReportService dataReportService;

    @Override
    public List<DataReportReqVo> queryNameList(String type) {
        LambdaQueryWrapper<DataBaseInfoEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(DataBaseInfoEntity::getIdentify, ReportConstant.DATA_COLLECTION);
        // 查询对应标识数据信息
        List<DataBaseInfoEntity> list = this.list(qw);

        // 获取某段时间内数据到报情况
        List<DataReportEntity> reportEntityList = dataReportService.dataRate(type, ReportConstant.DATA_COLLECTION);
        // 根据dataId 将数据分组
        Map<String, List<DataReportEntity>> map = Optional.ofNullable(reportEntityList).orElse(new ArrayList<>())
                .stream()
                .collect(Collectors.groupingBy(DataReportEntity::getDataId));
        return Optional.ofNullable(list).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    DataReportReqVo reportReqVo = new DataReportReqVo();
                    BeanUtils.copyProperties(item,reportReqVo);
                    // 到报率 (数据完整期次/总共多少期)
                    reportReqVo.setRate(this.getDataRate(map.get(item.getId())));
                    return reportReqVo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取数据期次到报率（数据完整期次/总共多少期）
     * @param list
     * @return
     */
    private String getDataRate(List<DataReportEntity> list) {
        // 当前时间段没有该数据，数据完整性为100%
        if (list ==null || list.isEmpty()) {
            return ReportConstant.ONE;
        }
        //过滤出数据完整的期次，（状态为1 数据完整）
        long count = Optional.of(list).orElse(new ArrayList<>())
                .stream()
                .filter(item -> Objects.equals(item.getStatus(), ReportConstant.STATUS_ONE))
                .count();
        return String.format("%.5f", (double) count / (double) list.size());
    }

}