package com.njht.webyun.management.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njht.webyun.management.sys.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-26 09:38:22
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

}
