package com.htht.executor.statistics.entity;

import lombok.Data;

/**
 * @author jiangjin
 * @email jiangjin@piesat.cn
 * @date 2022/9/29 14:49
 */
@Data
public class CategoryProductDto {
    /**产品名称
     *
     */
    private String name;

    /**
     * 产品ID
     */
    private String productId;

    /**周期
     *
     */
    private String cycle;

    /**
     * categgoryId
     */
    private String categoryId;

    /**
     * category的parentId
     */
    private String parentId;
}
