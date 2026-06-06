package com.njht.webyun.publish.product.controller;

import com.njht.webyun.common.UserUtil;
import com.njht.webyun.publish.product.service.DataCategoryService;
import com.njht.webyun.publish.product.service.ProductInfoService;
import com.njht.webyun.publish.product.service.ProductService;
import com.njht.webyun.publish.product.vo.*;
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
import java.util.Objects;

/**
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
@RestController
@RequestMapping("product/service")
@Api(value = "", tags = "产品服务")
public class ProductServiceController {
    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private DataCategoryService dataCategoryService;

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "热门产品")
    @GetMapping({"/hotProductList"})
    @ResponseBody
    public ReturnT<List<HotProductInfoReqVo>> getIndexProductInfo() {
        List<HotProductInfoReqVo> reqVoList = dataCategoryService.getHotProductInfo();
        return ReturnT.success(reqVoList);
    }

    @ApiOperation(value = "根据id查询产品列表")
    @PostMapping({"/getProductListById"})
    @ResponseBody
    public ReturnT<Map<String, Object>> getProductListById(@RequestBody @Validated ProductIdSearchVo searchVo) {
        Map<String, Object> map =
                productInfoService.getProductListById(searchVo.getId(), searchVo.getPage(), searchVo.getSize(),
                        searchVo.getRegionId(),searchVo.getGeo(),searchVo.getIsShow(),searchVo.getStartTime(),searchVo.getEndTime());
        if (Objects.isNull(map) || map.isEmpty()) {
            return ReturnT.failedMsg("没有数据");
        }
        return ReturnT.success(map);
    }


    @ApiOperation(value = "产品服务 多条件查询产品列表")
    @PostMapping({"/getProductList"})
    @ResponseBody
    public ReturnT<Map<String, Object>> getProductList(@RequestBody @Validated ProductInfoVo infoVo) {
        Map<String, Object> map = productInfoService.getProductList(infoVo);
        if (Objects.isNull(map) || map.isEmpty()) {
            return ReturnT.failedMsg("没有数据");
        }
        return ReturnT.success(map);
    }


    @ApiOperation(value = "产品服务  获取产品查询条件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "String", required = true, value = "产品id")
    })
    @GetMapping(value = "/getProductSearchInfo")
    @ResponseBody
    public ReturnT<List<ProductSearchVo>> getProductSearchInfo(@RequestParam String id) {
        List<ProductSearchVo> list = productService.getProductSearchInfo(id);
        return ReturnT.success(list);
    }


    @ApiOperation(value = "模糊检索查询list")
    @GetMapping({"/getFuzzySearchInfoList"})
    @ResponseBody
    public ReturnT<List<ProductFuzzySearchReqVo>> getFuzzySearchInfoList(){
        //获取userId 每个用户搜索框内容不同
        Integer userId = UserUtil.getCurrentUser().getUserId();
        List<ProductFuzzySearchReqVo> list = productInfoService.getFuzzySearchInfoList(userId);
        return ReturnT.success(list);
    }

    @ApiOperation(value = "模糊检索结果list")
    @PostMapping({"/getFuzzySearchProductList"})
    @ResponseBody
    public ReturnT<Map<String, Object>> getFuzzySearchProductList(@Validated @RequestBody ProductFuzzySearchVo searchVo){
        Map<String, Object> map = productInfoService.getFuzzySearchProductList(searchVo);
        if (Objects.isNull(map) || map.isEmpty()) {
            return ReturnT.failedMsg("没有数据");
        }
        return ReturnT.success(map);
    }
}
