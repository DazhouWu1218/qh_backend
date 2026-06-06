package com.njht.webyun.publish.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.entity.PageEntity;
import com.njht.webyun.publish.index.vo.IndexReqVo;
import com.njht.webyun.publish.product.entity.ProductInfoEntity;
import com.njht.webyun.publish.product.vo.ProductFuzzySearchReqVo;
import com.njht.webyun.publish.product.vo.ProductFuzzySearchVo;
import com.njht.webyun.publish.product.vo.ProductInfoReqVo;
import com.njht.webyun.publish.product.vo.ProductInfoVo;
import com.njht.webyun.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
public interface ProductInfoService extends IService<ProductInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);


    /**
     * 多条件查询产品列表
     * @param productInfoVo
     * @return
     */
    Map<String,Object> getProductList(ProductInfoVo productInfoVo);

    /**
     * 根据id查询产品列表
     * @param id
     * @param page
     * @param size
     * @param regionId
     * @param geo
     * @param isShow
     * @param startTime
     * @param endTime
     * @return
     */
    Map<String,Object> getProductListById(String id, Integer page, Integer size,
                                          String regionId, Map geo, Integer isShow,String startTime,String endTime);

    /**
     * 获取栅格数据信息
     * @param ids
     * @return
     */
    List<ProductInfoReqVo> getTifInfoList(List<String> ids);

    /**
     * 首页产品相关信息
     * @return
     */
    IndexReqVo getIndexInfo();

    /**
     * 模糊搜索
     * @param userId
     * @return
     */
    List<ProductFuzzySearchReqVo> getFuzzySearchInfoList(Integer userId);

    /**
     * 搜索框获取产品结果列表
     * @param searchVo
     * @return
     */
    Map<String, Object> getFuzzySearchProductList(ProductFuzzySearchVo searchVo);

    IPage<ProductInfoEntity> getProductInfoEntityIPage(ProductInfoVo productInfoVo, PageEntity pageEntity);

    /**
     * 根据id集合查询数据
     * @param idList
     * @return
     */
    List<ProductInfoEntity> entityList(List<String> idList);
}

