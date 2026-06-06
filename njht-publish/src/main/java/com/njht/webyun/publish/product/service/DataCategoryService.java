package com.njht.webyun.publish.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.entity.category.DataCategoryEntity;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.publish.product.vo.CategoryProductReqVo;
import com.njht.webyun.publish.product.vo.HotProductInfoReqVo;
import com.njht.webyun.publish.index.vo.IndexProductCategoryReqVo;
import com.njht.webyun.publish.index.vo.IndexProductReqVo;
import com.njht.webyun.publish.product.vo.ProductTree;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
public interface DataCategoryService extends IService<DataCategoryEntity> {

    /**
     * 列表查询
     * @return
     */
    List<CommonEntity> queryPage();

    /**
     * 查询树结构信息
     * @return
     */
    List<ProductTree> getCategoryTree();

    /**
     * 根据id 以及 名称集合
     * @param id
     * @return
     */
    Map<String,List<String>> getProductIdAndNames(String id);

    /**
     * 首页产品分类信息
     * @return
     */
    List<IndexProductCategoryReqVo> getFirstCategoryInfo();

    /**
     * 查询树结构信息
     * @param userId
     * @return
     */
    List<DataCategoryEntity> selectTreeList(Integer userId);

    /**
     * 获取产品名称以及产品Id
     * @param productId
     * @return
     */
    IndexProductReqVo getProductNameAndId(String productId);

    /**
     * 根据Product Id 获取父级名称
     * @param idList
     * @return
     */
    Map<String, String> getParentNameMapInfo(List<String> idList);

    /**
     * 获取热门产品信息
     * @return
     */
    List<HotProductInfoReqVo> getHotProductInfo();

    /**
     * 通过默认角色id查全部
     * @param roleId
     * @return
     */
    List<DataCategoryEntity> queryTreeListByRoleId(Integer roleId);

    /**
     * 首页轮播图信息
     * @return
     */
    List<DataCategoryEntity> getIndexProductInfo();

    List<Tree> getCategoryTreeByTreeKey(String type);

}

