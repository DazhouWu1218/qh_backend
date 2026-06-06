package com.htht.executor.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.executor.product.dao.ProductFileInfoDao;
import com.htht.executor.product.entity.ProductFileInfoEntity;
import com.htht.executor.product.service.ProductFileInfoService;
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
@Service("productFileInfoService")
public class ProductFileInfoServiceImpl extends ServiceImpl<ProductFileInfoDao, ProductFileInfoEntity> implements ProductFileInfoService {


    @Override
    public List<String> getDocFileInfoList() {
        LambdaQueryWrapper<ProductFileInfoEntity> qw = new LambdaQueryWrapper<>();
        // 查询状态为0 且没有转换的word
        qw.select(ProductFileInfoEntity::getFilePath);
        qw.eq(ProductFileInfoEntity::getProductType,"doc").eq(ProductFileInfoEntity::getZt,"0").isNotNull(ProductFileInfoEntity::getFilePath);
        return Optional.of(this.list(qw)).orElse(new ArrayList<>())
                .stream()
                .map(ProductFileInfoEntity::getFilePath)
                .collect(Collectors.toList());
    }


    @Override
    public List<Long> selectFileSize(String id, String issue, String cycle) {
        LambdaQueryWrapper<ProductFileInfoEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(ProductFileInfoEntity::getProductId,id);
        qw.eq(ProductFileInfoEntity::getCycle,cycle);
        qw.eq(ProductFileInfoEntity::getIssue,issue);
        return Optional.of(this.list(qw).stream().map(ProductFileInfoEntity::getFileSize).collect(Collectors.toList())).orElse(new ArrayList<>());
    }

    @Override
    public void updateByFilePath(String filePath) {
        baseMapper.updateZtByFilePath(filePath);
    }
}