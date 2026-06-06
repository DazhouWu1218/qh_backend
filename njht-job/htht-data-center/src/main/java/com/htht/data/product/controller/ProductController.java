package com.htht.data.product.controller;


import com.htht.data.product.entity.ProductEntity;
import com.htht.data.product.service.ProductService;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-01-05 09:15:29
 */
@RestController
@RequestMapping("/product/base")
@Api(value = "", tags = "产品基础信息")
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * 产品发布（支持批量发布产品）
     */
    @GetMapping("/list")
    @ApiOperation(value = "产品基础信息集合")
    public ReturnT<List<ProductEntity>> editProductStatus(){
        List<ProductEntity> list = productService.list();
        return ReturnT.success(list);
    }
}
