package com.njht.webyun.product.service;

import com.njht.webyun.product.vo.CategoryVo;
import com.njht.webyun.product.vo.ProductRoleTree;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/18 18:52
 * @Description: 产品目录权限控制
 */
public interface RoleCategoryService {

    /**
     * 获取树结构
     * @param roleId 角色id
     * @return
     */
    List<ProductRoleTree> getCategoryTree(Integer roleId);

    /**
     * 更新树结构的勾选状态
     * @param categoryVo 参数
     * @return
     */
    void updateRoleCategory(CategoryVo categoryVo);
}
