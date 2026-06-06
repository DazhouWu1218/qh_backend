package com.njht.webyun.management.business.dao;

import com.njht.webyun.management.business.entity.HthtDataCategory;
import com.njht.webyun.management.business.entity.ProductVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dgj
 */
@Repository
public interface ProductCategoryDao {

    /**
     * 查询
     * @param userId
     * @return
     */
    List<HthtDataCategory> selectCategoryByUserId(@Param("userId")String userId);

    /**
     * 查询第四级子节点信息
     * @param id
     * @return
     */
    List<ProductVo> selectProductInfo(@Param("id") String id);

    /**
     * 获取产品的父id
     * @param treeId
     * @return
     */
    String getParentId(String treeId);

    /**
     * 保存树结构信息
     * @param p
     */
    void saveData(HthtDataCategory p);

    /**
     * 通过名称查找id
     * @param name
     * @return
     */
    List<String> selectIdByName(@Param("name")String name);


    /**
     * 查询所有有子节点的id
     * @param userId
     * @return
     */
    List<String> selectParentIds(@Param("userId") String userId);

    /**
     * 查询树形结构信息
     * @param userId
     * @return
     */
    List<HthtDataCategory> selectCategoryInfo(String userId);

    /**
     * 查询树结构信息
     * @param identify
     * @return
     */
    List<HthtDataCategory> getProductTree(String identify);
}
