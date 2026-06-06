package com.njht.webyun.publish.sys.dao;

import com.njht.webyun.publish.sys.entity.RoleUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色表
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-30 20:05:56
 */
@Mapper
public interface RoleUserDao extends BaseMapper<RoleUserEntity> {
	
}
