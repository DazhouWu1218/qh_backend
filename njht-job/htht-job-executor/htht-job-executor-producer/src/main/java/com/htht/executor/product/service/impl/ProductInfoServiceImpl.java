package com.htht.executor.product.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.executor.product.dao.ProductInfoDao;
import com.htht.executor.product.entity.ProductInfoEntity;
import com.htht.executor.product.service.ProductInfoService;
import com.htht.executor.task.constant.BaseProductServiceConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 
 * @author daiguojun
 * @date 2022-05-10 18:06:35
 */
@Service("productInfoService")
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoDao, ProductInfoEntity> implements ProductInfoService {

    @Autowired
    private ProductInfoDao productInfoDao;


    @Override
    public boolean findProductExits(ProductInfoEntity productInfo) {
        LambdaQueryWrapper<ProductInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(productInfo.getCycle()),ProductInfoEntity::getCycle,productInfo.getCycle())
                .eq(StringUtils.isNotBlank(productInfo.getProductId()),ProductInfoEntity::getProductId,productInfo.getProductId())
                .eq(StringUtils.isNotBlank(productInfo.getMark()),ProductInfoEntity::getMark,productInfo.getMark())
                .eq(StringUtils.isNotBlank(productInfo.getIssue()),ProductInfoEntity::getIssue,productInfo.getIssue())
                .eq(StringUtils.isNotBlank(productInfo.getRegionId()),ProductInfoEntity::getRegionId,productInfo.getRegionId())
                .eq(StringUtils.isNotBlank(productInfo.getSatellite()),ProductInfoEntity::getSatellite,productInfo.getSatellite())
                .eq(StringUtils.isNotBlank(productInfo.getSensor()),ProductInfoEntity::getSensor,productInfo.getSensor());
        List<ProductInfoEntity> infos = Optional.of( productInfoDao.selectList(queryWrapper)).orElse(new ArrayList<>());
        return !infos.isEmpty();
    }

    @Override
    public List<String> queryDbIssueList(String productId, String cycle, String satellite) {
        LambdaQueryWrapper<ProductInfoEntity> qw = new LambdaQueryWrapper<>();
        qw.select(ProductInfoEntity::getIssue);
        qw.eq(ProductInfoEntity::getProductId,productId)
                .eq(ProductInfoEntity::getCycle,cycle)
                .eq(ProductInfoEntity::getSatellite,satellite)
                .eq(ProductInfoEntity::getRegionId, BaseProductServiceConstant.REGION_ID);
        List<ProductInfoEntity> dbInfoList = this.list(qw);
        return Optional.ofNullable(dbInfoList).orElse(new ArrayList<>())
                .stream()
                .map(ProductInfoEntity::getIssue)
                .collect(Collectors.toList());
    }
}