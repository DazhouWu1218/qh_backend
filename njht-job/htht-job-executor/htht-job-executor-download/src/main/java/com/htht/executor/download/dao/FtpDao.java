package com.htht.executor.download.dao;

import com.htht.executor.download.entity.FtpEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支撑平台-FTP配置信息表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-17 16:41:22
 */
@Mapper
public interface FtpDao extends BaseMapper<FtpEntity> {
	
}
