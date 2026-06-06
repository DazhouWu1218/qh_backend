package com.njht.webyun.product.service;

import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.product.dto.DataCategoryDto;
import com.njht.webyun.product.vo.ProductInfoTree;
import com.njht.webyun.product.vo.ProductVO;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/18 11:16
 * @Description: 树结构
 */
public interface ProductCategoryService {

    /**
     * 产品管理-产品树结构
     * @return
     */
    List<ProductInfoTree> getProductInfoTreeList();

    /**
     * 产品管理-编辑产品属性
     * @param productVO
     */
    void editProductInfo(ProductVO productVO);

    /**
     * 产品管理-产品新增
     * @param productVO
     */
    void addProductInfo(ProductVO productVO);

    /**
     * 产品管理-删除产品信息（删除要删除自己以及子节点信息，同时删除两张表）
     * @param id
     */
    void delProductInfo(String id);

    /**
     * 将category 集合转换成 tree集合
     * @param categoryEntityList
     * @return
     */
    List<ProductInfoTree> categoryListToTreeList(List<DataCategoryDto> categoryEntityList);

    /**
     * 周期对应关系
     * @return
     */
    List<CommonEntity> getCycleList();

    /**
     * 产品标识对应关系
     * @return
     */
    List<CommonEntity> getIdentifyList();
}
