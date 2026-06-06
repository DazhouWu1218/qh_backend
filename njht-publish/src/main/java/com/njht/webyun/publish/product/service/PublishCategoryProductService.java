package com.njht.webyun.publish.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.publish.product.entity.PublishCategoryProductEntity;
import com.njht.webyun.publish.product.vo.CategoryProductReqVo;
import com.njht.webyun.publish.product.vo.CategoryProductSearchVo;
import com.njht.webyun.utils.PageUtils;

import java.util.Map;

/**
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-08-18 16:51:47
 */
public interface PublishCategoryProductService extends IService<PublishCategoryProductEntity> {


    /**
     * 获取农气，生态页面数据查询条件
     * @param id
     * @return
     */
    CategoryProductReqVo getSearchInfo(String id);

    /**
     * 查询数据信息
     * @param searchVo
     * @return
     */
    PageUtils getMap(CategoryProductSearchVo searchVo);
}

