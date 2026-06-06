package com.njht.webyun.product.vo;

import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/18 15:13
 * @Description: 产品勾选状态实体类
 */
@Data
public class CategoryStatusVo {

    /**
     * 产品树id
     */
    private String categoryId;

    /**
     * 勾选状态  0不勾选，1勾选
     */
    private Integer status;
}

