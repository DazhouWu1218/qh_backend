package com.htht.data.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.data.product.dao.ProductDao;
import com.htht.data.product.entity.ProductEntity;
import com.htht.data.product.service.ProductService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 
 * @author daiguojun
 * @date 2022-01-05 09:15:31
 */
@Service("productService")
public class ProductServiceImpl extends ServiceImpl<ProductDao, ProductEntity> implements ProductService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductEntity> page = this.page(
                new Query<ProductEntity>().getPage(params),
                new QueryWrapper<ProductEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public String queryProductId(String treeId) {
        LambdaQueryWrapper<ProductEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(ProductEntity::getTreeId,treeId);
        ProductEntity one = this.getOne(qw);
        return one.getId();
    }

}