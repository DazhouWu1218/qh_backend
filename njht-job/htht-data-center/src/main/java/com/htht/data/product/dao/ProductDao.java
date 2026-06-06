package com.htht.data.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htht.data.product.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-01-05 09:15:31
 */
@Mapper
public interface ProductDao extends BaseMapper<ProductEntity> {
	
}
