package com.njht.webyun.product.controller;

import com.njht.webyun.entity.PageEntity;
import com.njht.webyun.product.service.HotProductService;
import com.njht.webyun.product.util.ProductPageUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/18 11:07
 * @Description: 热门产品信息
 */
@RestController
@RequestMapping("/system/product")
@Api(value = "产品管理-产品列表")
public class HotProductController {

    @Autowired
    private HotProductService hotProductService;

    /**
     * 热门产品列表
     * @return
     */
    @PostMapping({"/hotList"})
    @ResponseBody
    public ProductPageUtils getHotProductList(@RequestBody PageEntity pageEntity){
        return hotProductService.getHotProductList(pageEntity);
    }

    /**
     * 首页产品列表
     * @return
     */
    @PostMapping({"/indexList"})
    @ResponseBody
    public ProductPageUtils getIndexProductList(@RequestBody PageEntity pageEntity){
        return hotProductService.getIndexProductList(pageEntity);
    }

    /**
     * 热门产品新增 或 删除
     * @param id id
     * @param isHot 是否热门，1是 0 不是
     */
    @PostMapping("/addOrDelHotInfo")
    public void addOrDelHotProductInfo(@RequestParam("id") String id,@RequestParam("isHot")Integer isHot){
        hotProductService.addOrDelProductInfo(id,isHot);
    }

    /**
     * 热门产品新增 或 删除
     * @param id 产品id
     * @param isIndex 是否首页轮播 1是 0 不是
     */
    @PostMapping("/addOrDelIndexInfo")
    public void addOrDelIndexProductInfo(@RequestParam("id") String id,@RequestParam("isIndex")Integer isIndex){
        hotProductService.addOrDelIndexProductInfo(id,isIndex);
    }

}
