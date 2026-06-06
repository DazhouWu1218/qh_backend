package com.htht.executor.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htht.executor.product.entity.DicEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-22 11:54:17
 */
@Mapper
@Repository
public interface DicDao extends BaseMapper<DicEntity> {
	
}
