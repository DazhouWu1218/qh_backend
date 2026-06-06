package com.njht.webyun.publish.product.controller;

import java.util.List;

import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.utils.ReturnT;
import com.njht.webyun.publish.product.vo.ProductTree;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.njht.webyun.publish.product.service.DataCategoryService;


/**
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
@RestController
@RequestMapping("product/category")
@Api(value = "", tags = "产品分类")
public class DataCategoryController {
    @Autowired
    private DataCategoryService dataCategoryService;

    @ApiOperation(value = "分类树结构")
    @GetMapping({"/tree"})
    @ResponseBody
    public ReturnT<List<ProductTree>> getIndexProductInfo(){
        List<ProductTree> treeList = dataCategoryService.getCategoryTree();
        return ReturnT.success(treeList);
    }

    /**
     * 产品分类信息 模糊查询
     */
    @ApiOperation(value = "分类树结构模糊查询查询条件")
    @GetMapping("/list")
    @ResponseBody
    public ReturnT<List<CommonEntity>> list(){
        List<CommonEntity> commonEntityList = dataCategoryService.queryPage();
        return ReturnT.success(commonEntityList);
    }


}
