package com.njht.webyun.business.product.controller;


import com.njht.webyun.business.feign.DataCenterFeignService;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-01-05 09:15:29
 */
@RestController
@RequestMapping("product/info")
@Api(value = "", tags = "产品管理")
public class ProductInfoController {
    @Autowired
    private DataCenterFeignService dataCenterFeignService;

    /**
     * 产品发布（支持批量发布产品）
     */
    @PostMapping("/update")
    @ApiOperation(value = "产品发布（支持批量发布产品）")
    @ApiImplicitParam(paramType = "query", name = "ids", dataType = "List", required = true, value = "某几期产品对应id集合")
    public ReturnT update(@RequestParam("ids") List<String> ids){
        dataCenterFeignService.update(ids);
        return ReturnT.success();
    }

    /**
     * 产品删除（删除某一期产品信息）
     */
    @PostMapping("/delete")
    @ApiOperation(value = "产品删除（删除某一期产品信息）")
    @ApiImplicitParam(paramType = "query", name = "id", dataType = "String", required = true, value = "某一期产品对应id")
    public ReturnT delete(@RequestParam("id") String id){
        dataCenterFeignService.deleteProductById(id);
        return ReturnT.success();
    }


}
