package com.njht.webyun.product.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.PageInfo;
import com.njht.webyun.constant.DbConstant;
import com.njht.webyun.entity.PageEntity;
import com.njht.webyun.product.constant.ProductConstant;
import com.njht.webyun.product.dao.DataCategoryDao;
import com.njht.webyun.product.dto.DataCategoryDto;
import com.njht.webyun.product.entity.DataCategoryEntity;
import com.njht.webyun.product.service.HotProductService;
import com.njht.webyun.product.service.ProductCategoryService;
import com.njht.webyun.product.util.ProductPageUtils;
import com.njht.webyun.product.vo.ProductInfoTree;
import com.njht.webyun.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/25 16:49
 * @Description: 热门产品实现类
 */
@Service
@DS(value = DbConstant.MYSQL_1)
public class HotProductServiceImpl implements HotProductService {

    @Autowired
    private DataCategoryDao dataCategoryDao;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Override
    public ProductPageUtils getHotProductList(PageEntity pageEntity) {
        PageUtil.setPageAndSize(pageEntity.getPage(),pageEntity.getSize(),1,10);
        // 树结构中获取到产品相关信息
        List<DataCategoryDto> categoryEntityList = dataCategoryDao.selectCategoryAndProductList(ProductConstant.PRODUCT_IDENTIFY,null,ProductConstant.PRODUCT_STATUS_ONE);
        return this.getPageInfo(categoryEntityList);

    }

    @Override
    public ProductPageUtils getIndexProductList(PageEntity pageEntity) {
        PageUtil.setPageAndSize(pageEntity.getPage(),pageEntity.getSize(),1,10);
        // 树结构中获取到产品相关信息
        List<DataCategoryDto> categoryEntityList = dataCategoryDao.selectCategoryAndProductList(ProductConstant.PRODUCT_IDENTIFY,ProductConstant.PRODUCT_STATUS_ONE,null);
        return this.getPageInfo(categoryEntityList);
    }

    /**
     * 获取产品分页信息并返回
     * @param categoryEntityList
     * @return
     */
    private ProductPageUtils getPageInfo(List<DataCategoryDto> categoryEntityList) {
        // 产品分页
        ProductPageUtils pageUtils = new ProductPageUtils(new PageInfo<>(categoryEntityList));
        // category对象集合 转换成 Tree 对象集合
        List<ProductInfoTree> treeList = productCategoryService.categoryListToTreeList(categoryEntityList);
        pageUtils.setList(treeList);
        return pageUtils;
    }



    @Override
    public void addOrDelProductInfo(String id, Integer isHot) {
        DataCategoryEntity categoryEntity = new DataCategoryEntity();
        categoryEntity.setId(id);
        categoryEntity.setIsHot(isHot);
        dataCategoryDao.update(categoryEntity);

    }

    @Override
    public void addOrDelIndexProductInfo(String id, Integer isIndex) {
        DataCategoryEntity categoryEntity = new DataCategoryEntity();
        categoryEntity.setId(id);
        categoryEntity.setIsIndex(isIndex);
        dataCategoryDao.update(categoryEntity);
    }


}
