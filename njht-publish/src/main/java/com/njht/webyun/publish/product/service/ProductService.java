package com.njht.webyun.publish.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.publish.product.entity.ProductEntity;
import com.njht.webyun.publish.product.vo.ProductSearchVo;

import java.util.List;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
public interface ProductService extends IService<ProductEntity> {


    /**
     * 获取产品查询条件
     * @param id
     * @return
     */
    List<ProductSearchVo> getProductSearchInfo(String id);

    /**
     * 根据id集合 获取产品信息
     * @param idList
     * @return
     */
    List<String> getProductIdList(List<String> idList);


}

