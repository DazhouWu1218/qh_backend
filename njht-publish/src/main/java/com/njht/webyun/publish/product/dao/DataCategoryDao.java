package com.njht.webyun.publish.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njht.entity.category.DataCategoryEntity;
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
public interface DataCategoryDao extends BaseMapper<DataCategoryEntity> {

//    /**
//     * 树结构list
//     * @param userId
//     * @return
//     */
//    List<DataCategoryEntity> selectTreeList(@Param("userId") Integer userId,@Param("treeKey") String treeKey);
//
//    /**
//     * 查询父id信息
//     * @param userId
//     * @return
//     */
//    List<String> selectParentIds(@Param("userId") Integer userId);

    /**
     * 通过产品id 获取父级名称信息
     * @param idList
     * @return
     */
    List<DataCategoryEntity> getParentNameByProductIds(@Param("idList") List<String> idList);

    /**
     * 通过角色id查询树结构中的分类信息
     * @param roleId
     * @return
     */
    List<DataCategoryEntity> selectTreeListByRoleId(@Param("roleId") Integer roleId);
//
//    /**
//     * 查询所有的热门产品信息
//     * @param userId
//     * @return
//     */
//    List<DataCategoryEntity> selectHotProductList(@Param("userId")Integer userId);
}
