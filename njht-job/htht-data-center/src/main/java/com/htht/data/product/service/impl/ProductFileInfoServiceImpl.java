package com.htht.data.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.data.product.dao.ProductFileInfoDao;
import com.htht.data.product.entity.ProductFileInfoEntity;
import com.htht.data.product.service.ProductFileInfoService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 
 * @author daiguojun
 * @date 2022-01-05 09:15:31
 */
@Service("productFileInfoService")
public class ProductFileInfoServiceImpl extends ServiceImpl<ProductFileInfoDao, ProductFileInfoEntity> implements ProductFileInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductFileInfoEntity> page = this.page(
                new Query<ProductFileInfoEntity>().getPage(params),
                new QueryWrapper<ProductFileInfoEntity>()
        );

        return new PageUtils(page);
    }

}