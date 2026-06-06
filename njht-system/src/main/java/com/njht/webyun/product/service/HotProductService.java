package com.njht.webyun.product.service;

import com.njht.webyun.entity.PageEntity;
import com.njht.webyun.product.util.ProductPageUtils;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/25 16:47
 * @Description: 热门产品相关接口
 */
public interface HotProductService {

    /**
     * 获取 热门产品集合
     * @param pageEntity 分页
     * @return 产品信息
     */
    ProductPageUtils getHotProductList(PageEntity pageEntity);

    /**
     * 获取首页轮播列表
     * @param pageEntity 分页
     * @return 产品信息
     */
    ProductPageUtils getIndexProductList(PageEntity pageEntity);

    /**
     * 新增 或者 删除 产品
     * @param id
     * @param isHot
     */
    void addOrDelProductInfo(String id, Integer isHot);

    /**
     * 新增 或者 删除 轮播图相关信息
     * @param id
     * @param isIndex
     */
    void addOrDelIndexProductInfo(String id, Integer isIndex);
}
