package com.njht.webyun.publish.sys.dao;

import com.njht.webyun.publish.sys.entity.RoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-30 20:05:56
 */
@Mapper
public interface RoleDao extends BaseMapper<RoleEntity> {

    /**
     * 查询角色名称
     * @param userId
     * @return
     */
    String selectRoleNameByUserId(@Param("userId")Integer userId);

    /**
     * 查询菜单id
     * @param userId
     * @return
     */
    String selectFunCodeByUserId(Integer userId);
}
