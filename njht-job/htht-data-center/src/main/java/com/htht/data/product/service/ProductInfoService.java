package com.htht.data.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.data.product.entity.ProductInfoEntity;

import java.util.List;

/**
 * @author 产品服务
 * @email daiguojun@piesat.cn
 * @date 2022-01-05 09:15:29
 */
public interface ProductInfoService extends IService<ProductInfoEntity> {


    /**
     * 修改产品状态为发布
     * @param ids
     */
    void editProductStatus(List<String> ids);

    /**
     * 通过产品id 删除产品信息
     * @param id
     */
    void deleteProductById(String id);

}

