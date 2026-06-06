package com.njht.webyun.product.dao;

import com.njht.webyun.product.dto.DataCategoryDto;
import com.njht.webyun.product.entity.DataCategoryEntity;
import com.njht.webyun.product.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
@Mapper
public interface DataCategoryDao {

    /**
     * 新增
     * @param categoryEntity 树结构信息
     */
    void insert(DataCategoryEntity categoryEntity);

    /**
     * 修改
     * @param categoryEntity 树结构信息
     */
    void update(DataCategoryEntity categoryEntity);


    /**
     * 树结构列表
     * @return
     */
    List<DataCategoryEntity> selectCategoryList();

    /**
     * 根据角色id查询对应的产品树id
     * @param roleId 角色id
     * @return
     */
    List<String> selectCategoryIdList(@Param("roleId") Integer roleId);


    /**
     * 查询产品信息以及树结构信息
     * @param productIdentify
     * @param isIndex
     * @param isHot
     * @return
     */
    List<DataCategoryDto> selectCategoryAndProductList(@Param("productIdentify") String productIdentify, @Param("isIndex") Integer isIndex,
                                                       @Param("isHot") Integer isHot);

    /**
     * 通过id 查询menu 和Text 信息
     * @param id 主键id
     * @return
     */
    DataCategoryEntity selectMenuInfoById(@Param("id") String id);

    /**
     * 批量删除
     * @param idList 要删除的id集合
     */
    void deleteByIds(@Param("idList") List<String> idList);

    /**
     * 通过树id查询产品id
     * @param treeId
     * @return
     */
    ProductEntity selectProductId(@Param("treeId") String treeId);

    /**
     * 查询id 对应的子集信息
     * @param id
     * @return
     */
    List<DataCategoryEntity> selectChildCategoryListById(String id);
}
