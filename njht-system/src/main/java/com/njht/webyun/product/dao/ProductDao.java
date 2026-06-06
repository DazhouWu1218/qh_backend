package com.njht.webyun.product.dao;

import com.njht.webyun.product.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/19 18:09
 * @Description: 产品dao层
 */
@Mapper
public interface ProductDao {

    /**
     * 新增
     * @param productEntity 实体类
     */
    void insert(ProductEntity productEntity);

    /**
     * 修改
     * @param productEntity 实体类
     */
    void update(ProductEntity productEntity);


    /**
     * 通过 树结构id 批量删除
     * @param idList treeId 集合
     */
    void deleteByIds(@Param("idList") List<String> idList);

    /**
     * 查询该产品id 对应的产品总数
     * @param idList 树结构id
     * @return
     */
    Integer selectCountByProductIdList(@Param("idList")List<String> idList);
}
