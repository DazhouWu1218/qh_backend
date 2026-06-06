package com.njht.webyun.management.upload.dao;

import com.njht.webyun.management.upload.entity.BusinessStatisticEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/6/23 16:08
 * @Description: java类作用描述
 */
@Repository
public interface DataAnalysisDao {

    /**
     * 业务产品查询统计分析相关的结果
     * @return
     */
    List<BusinessStatisticEntity> getBusinessStatisticInfo();

    /**
     * 统计所有业务产品文件大小总量
     * @return
     */
    Long countAllBusinessStatisticInfo();
}
