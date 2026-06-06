package com.njht.webyun.product.controller;

import com.njht.webyun.product.service.ProductImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/24 10:24
 * @Description: 图片上传下载
 */
@RequestMapping("/system/product/image")
@RestController
@Slf4j
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    /**
     * 上传图片并返回图片路径
     * @param file
     * @return
     */
    @PostMapping(value = "upload")
    public String importExcel(@RequestParam("file") MultipartFile file) {
        return productImageService.uploadFile(file);
    }
}
