package com.htht.executor.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.executor.product.entity.ProductInfoEntity;

import java.util.List;

/**
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-10 18:06:35
 */
public interface ProductInfoService extends IService<ProductInfoEntity> {

    /**
     * 判断产品是否存在，多条件查询
     * @param queryDto
     * @return
     */
     boolean findProductExits (ProductInfoEntity queryDto);

    /**
     * 查询该产品以入库期次信息
     * @param productId
     * @param cycle
     * @param satellite
     * @return
     */
    List<String> queryDbIssueList(String productId, String cycle, String satellite);
}

