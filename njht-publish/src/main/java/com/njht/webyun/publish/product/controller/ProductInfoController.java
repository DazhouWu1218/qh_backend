package com.njht.webyun.publish.product.controller;

import com.njht.webyun.publish.product.service.ProductInfoService;
import com.njht.webyun.publish.product.service.ProductService;
import com.njht.webyun.publish.product.vo.ProductInfoVo;
import com.njht.webyun.publish.product.vo.ProductSearchVo;
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
@RequestMapping("product/info")
@Api(value = "", tags = "产品高级检索")
public class ProductInfoController {
    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductService productService;


    @ApiOperation(value = "高级检索获取产品详情")
    @PostMapping({"/getProductList"})
    @ResponseBody
    public ReturnT<Map<String,Object>> getProductList(@Validated @RequestBody ProductInfoVo infoVo){
        Map<String, Object> map = productInfoService.getProductList(infoVo);
        if(Objects.isNull(map) || map.isEmpty()){
            return ReturnT.failedMsg("没有数据");
        }
        return ReturnT.success(map);
    }


    @ApiOperation(value = "获取高级检索相关的查询条件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="String", name="id", dataType="String", required=true, value="产品id")
    })
    @PostMapping({"/getProductSearchInfo"})
    @ResponseBody
    public ReturnT<List<ProductSearchVo>> getProductSearchInfo(@RequestParam("id")String id){
        List<ProductSearchVo> list = productService.getProductSearchInfo(id);
        return ReturnT.success(list);
    }

}
