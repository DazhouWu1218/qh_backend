package com.njht.webyun.product.vo;

import com.njht.webyun.entity.Tree;
import lombok.Data;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/19 13:44
 * @Description: 产品目录以及对应产品信息树
 */
@Data
public class ProductInfoTree extends Tree {

    /**
     * 所属分级
     */
    private String menu;

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
    private Integer isRelease = 0;

    /**
     * 产品路径
     */
    private String productPath;

    private String  treeKey;



}
