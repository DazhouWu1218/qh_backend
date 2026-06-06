package com.htht.data.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.data.product.entity.ProductEntity;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.ReturnT;

import java.util.Map;

/**
 * @author 产品调度
 * @email daiguojun@piesat.cn
 * @date 2022-01-05 09:15:31
 */
public interface ProductService extends IService<ProductEntity> {

    PageUtils queryPage(Map<String, Object> params);

    String queryProductId(String treeId);
}

