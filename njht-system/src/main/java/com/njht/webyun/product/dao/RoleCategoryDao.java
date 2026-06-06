package com.njht.webyun.product.dao;

import com.njht.webyun.product.entity.RoleCategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/18 18:54
 * @Description: 产品目录权限dao层
 */
@Mapper
public interface RoleCategoryDao {

    /**
     * 新增角色产品对应关系
     * @param roleCategoryEntityList 角色产品id对应集合
     */
    void insertCategoryRoleList(@Param("roleCategoryEntityList") List<RoleCategoryEntity> roleCategoryEntityList);


    /**
     * 刪除角色产品对应关系
     * @param roleId 角色id
     * @param delCategoryIdList 角色产品id对应集合
     */
    void deleteCategoryRoleList(@Param("roleId") Integer roleId, @Param("delCategoryIdList") List<String> delCategoryIdList);

    /**
     * 根据角色ID 查询对应有权限的树结构信息
     * @param idList 角色 id 集合
     * @return
     */
    List<String> selectCategoryIdList(@Param("idList") List<Integer> idList);
}
