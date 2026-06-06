package com.htht.executor.product.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.htht.executor.product.entity.ProductInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
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
public interface ProductInfoDao extends BaseMapper<ProductInfoEntity> {
	
}
