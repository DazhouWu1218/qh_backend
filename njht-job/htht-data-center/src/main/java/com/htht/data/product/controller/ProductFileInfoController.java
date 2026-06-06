package com.htht.data.product.controller;


import com.htht.data.product.service.ProductFileInfoService;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-01-05 09:15:29
 */
@RestController
@RequestMapping("product/fileInfo")
@Api(value = "", tags = "产品管理")
public class ProductFileInfoController {
    @Autowired
    private ProductFileInfoService productFileInfoService;


    @GetMapping("count")
    @ResponseBody
    public ReturnT<Integer> count(){
        return ReturnT.success(productFileInfoService.count());
    }



}
