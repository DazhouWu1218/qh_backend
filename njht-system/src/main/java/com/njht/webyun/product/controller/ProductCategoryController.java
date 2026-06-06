package com.njht.webyun.product.controller;

import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.product.service.ProductCategoryService;
import com.njht.webyun.product.vo.ProductInfoTree;
import com.njht.webyun.product.vo.ProductVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/18 11:07
 * @Description: 产品树结构控制层
 */
@RestController
@RequestMapping("/system/product")
@Api(value = "产品管理-产品列表")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 周期集合 （周期对应关系-字典）
     * @return
     */
    @GetMapping({"/cycleList"})
    @ResponseBody
    public List<CommonEntity> getCycleList(){
        return productCategoryService.getCycleList();
    }

    /**
     * 产品标识集合
     */
    @GetMapping({"/identifyList"})
    @ResponseBody
    public List<CommonEntity> getIdentifyList(){
        return productCategoryService.getIdentifyList();
    }

    /**
     * 树集合
     * @return
     */
    @GetMapping({"/list"})
    @ResponseBody
    public List<ProductInfoTree> getProductInfoTreeList(){
        return productCategoryService.getProductInfoTreeList();
    }


    /**
     * 修改产品目录
     * @param productVO 产品参数
     */
    @PostMapping("edit")
    public void editProductInfo(@RequestBody ProductVO productVO){
        productCategoryService.editProductInfo(productVO);
    }


    /**
     * 产品新增
     * @param productVO
     */
    @PostMapping("add")
    public void addProductInfo(@RequestBody ProductVO productVO){
        productCategoryService.addProductInfo(productVO);
    }

    /**
     * 产品删除
     * @param id
     */
    @PostMapping("delete")
    public void delProductInfo(@RequestParam("id")String id){
        productCategoryService.delProductInfo(id);
    }
}
