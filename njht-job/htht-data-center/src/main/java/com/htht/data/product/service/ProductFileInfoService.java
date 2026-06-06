package com.htht.data.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.data.product.entity.ProductFileInfoEntity;
import com.njht.webyun.utils.PageUtils;

import java.util.Map;

/**
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-01-05 09:15:31
 */
public interface ProductFileInfoService extends IService<ProductFileInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

