package com.njht.webyun.publish.product.controller;

import com.njht.webyun.entity.Tree;
import com.njht.webyun.publish.product.service.DataCategoryService;
import com.njht.webyun.publish.product.service.PublishCategoryProductService;
import com.njht.webyun.publish.product.vo.CategoryProductReqVo;
import com.njht.webyun.publish.product.vo.CategoryProductSearchVo;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author daiguojun
 * @date 2022-08-20 14:58
 * 农气生态
 */
@RestController
@RequestMapping("product/eco")
@Api(value = "", tags = "农气生态")
public class EcoInfoController {

    @Autowired
    private DataCategoryService dataCategoryService;

    @Autowired
    private PublishCategoryProductService publichCategoryProductService;

    @ApiOperation(value = "生态，农气树结构")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="String", name="type", dataType="String", required=false, value="type nq 农气 eco 生态")
    })
    @PostMapping({"/tree"})
    @ResponseBody
    public ReturnT<List<Tree>> tree(@RequestParam("type") String type){
        List<Tree> treeList = dataCategoryService.getCategoryTreeByTreeKey(type);
        return ReturnT.success(treeList);
    }

    @ApiOperation(value = "生态，农气搜素框相关信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="String", name="id", dataType="String", required=false, value="树结构id")
    })
    @PostMapping({"/searchInfo"})
    @ResponseBody
    public ReturnT<CategoryProductReqVo> searchInfo(@RequestParam("id") String id){
        CategoryProductReqVo reqVo = publichCategoryProductService.getSearchInfo(id);
        return ReturnT.success(reqVo);
    }

    @ApiOperation(value = "生态，农气数据结果集")
    @PostMapping({"/info"})
    @ResponseBody
    public ReturnT<PageUtils> getInfo(@Validated @RequestBody CategoryProductSearchVo searchVo){
        PageUtils map = publichCategoryProductService.getMap(searchVo);
        return ReturnT.success(map);
    }
}
