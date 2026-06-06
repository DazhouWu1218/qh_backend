package com.njht.webyun.business.datapush.service.impl;


import com.njht.entity.dataPush.ProductStatusEntity;
import com.njht.entity.dataReport.DataReportEntity;
import com.njht.webyun.business.datapush.constant.EsConstant;
import com.njht.webyun.business.datapush.service.DataPushService;
import com.njht.webyun.business.report.service.DataReportService;
import com.njht.webyun.enums.CycleTypeEnum;
import com.njht.webyun.enums.IdentifyTypeEnum;
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
 * 产品生产情况统计 实现类
 */
@Service(EsConstant.PRODUCT_COLLECT)
@Slf4j
public class ProductPushServiceImpl implements DataPushService {


    @Autowired
    private DataReportService dataReportService;

    public List<?> execute() {
        log.info("产品生产情况统计");
        // 获取当天数据到达情况
        return this.queryProductStatusList(dataReportService.queryDiProductList());
    }

    /**
     * 处理参数并返回
     * @param dbList
     * @return
     */
    private List<ProductStatusEntity> queryProductStatusList(List<DataReportEntity> dbList) {
        return Optional.ofNullable(dbList).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    ProductStatusEntity productStatusEntity = new ProductStatusEntity();
                    productStatusEntity.setProductType(IdentifyTypeEnum.getValue(item.getIdentify()));
                    productStatusEntity.setProductName(item.getDataName());
                    productStatusEntity.setProductCycle(CycleTypeEnum.getValue(item.getCycle()));
                    productStatusEntity.setProductPeriod(item.getIssue());
                    productStatusEntity.setDataNumber(item.getFileNum().intValue());
                    String rate = String.format("%.3f", (double) item.getFileNum() / (double) item.getSumNum());
                    productStatusEntity.setDataIntegrity(rate);
                    return productStatusEntity;
                }).sorted(Comparator.comparing(ProductStatusEntity::getProductType).thenComparing(ProductStatusEntity::getProductPeriod))
                .collect(Collectors.toList());
    }

}
