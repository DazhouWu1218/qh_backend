package com.njht.webyun.product.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/18 14:10
 * @Description: 角色数据对应实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleCategoryEntity {

    private Integer roleId;
    private String categoryId;
}
