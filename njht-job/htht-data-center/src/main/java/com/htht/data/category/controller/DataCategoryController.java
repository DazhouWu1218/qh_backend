package com.htht.data.category.controller;

import com.htht.data.category.service.DataCategoryService;
import com.htht.data.product.service.ProductService;
import com.njht.entity.category.DataCategoryEntity;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
@RestController
@RequestMapping("/category")
@Api(tags = "数据分类")
public class DataCategoryController {
    @Autowired
    private DataCategoryService dataCategoryService;

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "根据权限以及标识查询category信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="String", name="treeKey", value="分类标识,null查全部"),
            @ApiImplicitParam(paramType="String", name="parentId",  value="父id,只查询父id为某一类的数据")
    })
    @PostMapping({"/list"})
    @ResponseBody
    public ReturnT<List<DataCategoryEntity>> categoryList(@RequestParam(value = "treeKey",required = false) String treeKey,
                                                                 @RequestParam(value = "parentId",required = false) String parentId){
        List<DataCategoryEntity> categoryEntityList = dataCategoryService.getCategoryList(treeKey,parentId);
        return ReturnT.success(categoryEntityList);
    }


    @PostMapping({"/getById"})
    @ResponseBody
    public ReturnT<DataCategoryEntity> categoryList(@RequestParam(value = "id") String id){
        DataCategoryEntity categoryEntity = dataCategoryService.getById(id);
        return ReturnT.success(categoryEntity);
    }

    @ApiOperation(value = "查询产品标识信息")
    @GetMapping({"treeKeyList"})
    public ReturnT<List<String>> treeKeyList(){
        List<String> treeKeyList = dataCategoryService.queryTreeKeyList();
        return ReturnT.success(treeKeyList);
    }

    @ApiOperation(value = "查询产品标识信息")
    @GetMapping({"treeList"})
    public ReturnT<List<DataCategoryEntity>> treeList(){
        List<DataCategoryEntity> treeList = dataCategoryService.list();
        return ReturnT.success(treeList);
    }

    @PostMapping("/getParentCategoryList")
    public ReturnT<List<DataCategoryEntity>> getParentCategoryInfoByProductIds(@RequestParam("productIds") List<String> productIds){
        List<DataCategoryEntity> categoryEntityList = dataCategoryService.getParentCategoryInfoByProductIds(productIds);
        return ReturnT.success(categoryEntityList);
    }

    @PostMapping("/getProductId")
    public ReturnT<String> getProductId(@RequestParam(value = "treeId",required = false) String treeId){
        String productId = productService.queryProductId(treeId);
        return ReturnT.success(productId);
    }
}
