package com.htht.executor.product.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.htht.executor.product.entity.ProductFileInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-10 18:06:35
 */
@DS("mysql")
@Mapper
@Repository
public interface ProductFileInfoDao extends BaseMapper<ProductFileInfoEntity> {

    /**
     * 修改状态
     * @param filePath
     */
    void updateZtByFilePath(@Param("filePath") String filePath);
}
