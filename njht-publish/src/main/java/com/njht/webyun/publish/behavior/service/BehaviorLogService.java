package com.njht.webyun.publish.behavior.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.entity.category.DataCategoryEntity;
import com.njht.webyun.publish.behavior.entity.BehaviorLogEntity;
import com.njht.webyun.publish.behavior.vo.StatisticSearchVo;
import com.njht.webyun.publish.product.entity.ProductFileInfoEntity;
import com.njht.webyun.publish.product.entity.ProductInfoEntity;
import com.njht.webyun.utils.PageUtils;

import java.util.List;

/**
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-12 14:55:20
 */
public interface BehaviorLogService extends IService<BehaviorLogEntity> {

    /**
     * 查询列表信息
     * @param statisticSearchVo
     * @return
     */
    PageUtils queryPage(StatisticSearchVo statisticSearchVo);


    /**
     * 保存用户下载记录记录到日志表
     * @param fileInfoEntityList
     * @param userId
     */
    void saveProductDownLoadInfo(List<ProductFileInfoEntity> fileInfoEntityList,Integer userId,List<ProductInfoEntity> productInfoEntities,List<DataCategoryEntity> categoryEntities);


    /**
     * 查询用户访问量
     * @return
     */
    Integer queryViewCountInfo();
}

