package com.njht.webyun.business.report.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njht.entity.dataReport.DataBaseInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据监控基础信息
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-08-16 15:49:24
 */
@Mapper
public interface DataBaseInfoDao extends BaseMapper<DataBaseInfoEntity> {
	
}
