package com.njht.webyun.management.upload.service;

import com.njht.webyun.management.upload.dto.BusinessStatisticDTO;
import com.njht.webyun.management.upload.dto.DiskCapacityDTO;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/6/23 10:42
 * @Description: 数据管理统计分析
 */
public interface DataAnalysisService {

    /**
     * 磁盘容量统计
     *
     * @return
     */
    DiskCapacityDTO getDiskCapacity();

    /**
     * 业务产品生产模块统计分析
     *
     * @return
     */
    List<BusinessStatisticDTO> getBusinessStatisticInfo();

    /**
     * 将统计分析的结果添加到缓存中用的时候在缓存中取，缓存时效设置为1小时，每一小时重新进行一次统计
     *
     * @return
     */
    List<BusinessStatisticDTO> getBusinessStatisticEntityList();


    /**
     * 获取预处理数据总量
     *
     * @return
     */
    Long getPretreatmentProduct();

    /**
     * 业务产品生产模块统计分析 将所有文件大小累加
     *
     * @return
     */
    Long countAllBusinessStatisticInfo();
}
