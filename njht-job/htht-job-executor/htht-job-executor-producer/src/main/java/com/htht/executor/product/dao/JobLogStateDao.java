package com.htht.executor.product.dao;

import com.htht.executor.product.entity.JobLogStateEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author chenzedong
 * @email chenzedong@piesat.cn
 * @date 2022-05-26 14:11:02
 */
@Repository
@Mapper
public interface JobLogStateDao extends BaseMapper<JobLogStateEntity> {
	
}
