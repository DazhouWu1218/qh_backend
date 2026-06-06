package com.njht.webyun.business.report.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.entity.dataReport.DataBaseInfoEntity;
import com.njht.webyun.business.report.vo.DataReportReqVo;

import java.util.List;

/**
 * 数据监控基础信息
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-08-16 15:49:24
 */
public interface DataBaseInfoService extends IService<DataBaseInfoEntity> {


    /**
     * 查询下载数据集合
     */
    List<DataReportReqVo> queryNameList(String type);
}

