package com.htht.data.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.entity.category.DataCategoryEntity;

import java.util.List;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
public interface DataCategoryService extends IService<DataCategoryEntity> {

    /**
     * 通过角色id 查询树分类
     * @param roleIdList
     * @param treeKey
     * @param parentId
     * @return
     */
    List<DataCategoryEntity> queryCategoryListByRoleId(List<String> roleIdList,String treeKey,String parentId);

    /**
     * 查询树分类信息
     * @param treeKey
     * @param parentId
     * @return
     */
    List<DataCategoryEntity> getCategoryList(String treeKey, String parentId);

    /**
     * 查询树结构标识
     * @return
     */
    List<String> queryTreeKeyList();

    /**
     * 根据id 查询
     * @param idList
     * @return
     */
    List<DataCategoryEntity> getParentCategoryInfoByProductIds(List<String> idList);
}

