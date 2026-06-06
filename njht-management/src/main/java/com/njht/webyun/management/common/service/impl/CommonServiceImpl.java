package com.njht.webyun.management.common.service.impl;

import com.njht.webyun.entity.Tree;
import com.njht.webyun.management.business.dao.ProductCategoryDao;
import com.njht.webyun.management.business.entity.HthtDataCategory;
import com.njht.webyun.management.common.service.CommonService;
import com.njht.webyun.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：daiguojun
 * @date ：Created in 2021/3/3 16:36
 * @description：
 */
@Service("commonService")
public class CommonServiceImpl  implements CommonService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Override
    public ReturnT<Object> getProductTree(String identify) {
//        List<HthtDataCategory> dataCategoryList =  productCategoryDao.getProductTree(identify);
//        List<Tree> treeList = this.changeServiceListToServiceTree(dataCategoryList);
//        List<Tree> treeList1 = TreeBuilderUtil.buildTreeList(treeList);
        return ReturnT.success();
    }

    /**
     *
     * @param dataCategoryList
     * @return
     */
    public List<Tree> changeServiceListToServiceTree(List<HthtDataCategory> dataCategoryList){
        return dataCategoryList.stream().map(s -> {
            Tree tree = new Tree();
            tree.setValue(s.getId());
            tree.setLabel(s.getName());
            tree.setParentId(s.getParentId());
            return tree;
        }).collect(Collectors.toList());
    }


}
