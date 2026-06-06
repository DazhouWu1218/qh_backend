package com.htht.job.admin.ftp.dao;

import com.htht.job.admin.ftp.entity.FtpEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调度平台-FTP配置信息表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-06-27 17:05:24
 */
@Mapper
public interface FtpDao extends BaseMapper<FtpEntity> {
	
}
