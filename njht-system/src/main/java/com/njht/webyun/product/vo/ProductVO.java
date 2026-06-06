package com.njht.webyun.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/25 10:52
 * @Description: 产品管理 编辑新增 前端传参
 */
@Data
public class ProductVO {

    /**
     * 主键id
     */
    private String id;
    private String name;
    private String parentId;

    /**
     * 排序字段
     */
    private Integer sortKey;

    /**
     * 产品图片路径
     */
    private String imgUrl;

    /**
     * 热门产品路径
     */
    private String hotImgUrl;

    /**
     * 热门产品（1是 0不是）
     */
    private Integer isHot = 0;

    /**
     * 首页轮播图（1是 0不是）
     */
    private Integer isIndex = 0;

    /**
     * 标识
     */
    private String mark;

    /**
     * 周期
     */
    private List<String> cycleList;

    /**
     * 是否需要审核（1 不审核/直接发布,0 审核/未发布）
     */
    private Integer isRelease;

    /**
     * 产品路径
     */
    private String productPath;

    /**
     * 备注信息
     */
    private String bz;

    private String treeKey;
}
