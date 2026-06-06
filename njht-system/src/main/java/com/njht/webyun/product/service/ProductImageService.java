package com.njht.webyun.product.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/24 10:31
 * @Description: 处理产品相关图片上传下载
 */
public interface ProductImageService {

    /**
     * 上传图片并返回图片相对路径
     * @param file 上传的文件
     * @return
     * @throws Exception
     */
    String uploadFile(MultipartFile file);
}
