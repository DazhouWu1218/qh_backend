package com.njht.webyun.management.business.service;


import com.njht.webyun.entity.Tree;
import com.njht.webyun.management.business.entity.ProductParam;
import com.njht.webyun.management.business.entity.ProductSelectForm;
import com.njht.webyun.management.business.vo.BusinessParam;
import com.njht.webyun.utils.ReturnT;

import java.util.List;

/**
 * @author dgj
 */
public interface BusinessProductService {

    /**
     * 根据筛选条件检索，查找相对应的产品信息
     * @param productParam
     * @param regions
     * @return
     */
    ReturnT<Object> getProjectInfos(ProductParam productParam, List<String> regions);

    /**
     * 查询业务产品三级
     * @param userId
     * @return
     */
    List<Tree> findProductTreeByUid(String userId);


    /**
     * 查询业务产品树
     * @return
     */
    List<Tree> findProductTree();

    /**
     * 通过第三级的id查询产品的周期以及数据源
     * @param id
     * @return
     */
    ProductSelectForm getCycleAndDataSourceById(String id);

    /**
     * 模糊查询目录结构
     * @param userId
     * @return
     */
    List<Object> getProductList(String userId);

    /**
     * 业务产品对外提供的
     * @param productParam
     * @param regions
     * @return
     */
    ReturnT<Object> getBusinessProductInfo(BusinessParam productParam, List<String> regions);

    /**
     * 查询快试图信息
     * @param id
     * @return
     */
    ReturnT<Object> getPngInfo(String id);

    /**
     * 查询父级id
     * @param id
     * @return
     */
    ReturnT<Object> getParentListInfo(String id);


    /**
     * word转pdf
     */
    void word2Pdf();
}
