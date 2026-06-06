package com.njht.webyun.business.report.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.entity.dataReport.DataReportEntity;
import com.njht.webyun.business.report.vo.DataDetailReportVo;
import com.njht.webyun.business.report.vo.ProductReportVo;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.utils.PageUtils;

import java.util.List;

/**
 * 数据下载信息统计表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-08-16 15:49:24
 */
public interface DataReportService extends IService<DataReportEntity> {


    /**
     * 数据到报率 （期次完整个数/总个数）
     * @param type
     * @param identify
     * @return
     */
    List<DataReportEntity> dataRate(String type, String identify);

    /**
     * 获取某个数据到报率情况
     * @param dataDetailReportVo
     * @return
     */
    PageUtils queryDataReportList(DataDetailReportVo dataDetailReportVo);

    /**
     * 获取当天预处理结果
     * @param reportVo
     * @return
     */
    PageUtils queryDataProducerList(ProductReportVo reportVo);

    /**
     * 产品列表
     * @return
     */
    List<CommonEntity> queryProductList();

    /**
     * 当天数据生产情况统计 （天境）
     * @return
     */
    List<DataReportEntity> queryDataList();

    /**
     * 当天预处理以及产品生产情况（天境）
     * @return
     */
    List<DataReportEntity> queryDiProductList();
}

