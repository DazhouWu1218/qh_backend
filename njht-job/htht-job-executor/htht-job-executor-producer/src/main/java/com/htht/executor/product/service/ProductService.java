package com.htht.executor.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.executor.product.entity.ProductEntity;
import com.htht.executor.statistics.entity.CategoryProductDto;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-10 18:06:35
 */
public interface ProductService extends IService<ProductEntity> {

    /**
     * 查询树结构最后一级信息
     * @param treeKey
     * @return
     */
    List<CategoryProductDto> selectProductBaseCfgInfo(String treeKey);

    /**
     * 查询 日资料相关信息
     * @param txtCols
     * @param tableName
     * @param issue
     * @param cycle

     * @return
     */
    List<Map<String, Object>> selectCimisInfoFromTable(String txtCols, String tableName,String issue,String cycle);
}

