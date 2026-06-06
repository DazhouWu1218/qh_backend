package com.htht.executor.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.executor.product.entity.ProductFileInfoEntity;

import java.util.List;

/**
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-10 18:06:35
 */
public interface ProductFileInfoService extends IService<ProductFileInfoEntity> {

    /**
     * 获取没有转换成pdf 的数据
     * @return
     */
    List<String> getDocFileInfoList();

    /**
     * 查询文件大小
     * @param id
     * @param issue
     * @param cycle
     * @return
     */
    List<Long> selectFileSize(String id, String issue, String cycle);

    /**
     * 根据路径修改状态
     * @param filePath
     */
    void updateByFilePath(String filePath);
}

