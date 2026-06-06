package com.njht.webyun.publish.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.publish.sys.entity.RoleEntity;

import java.util.Map;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-30 20:05:56
 */
public interface RoleService extends IService<RoleEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 通过用户id 查询角色名称
     * @param userId
     * @return
     */
    String getRoleNameByUserId(Integer userId);

    /**
     *通过用户id 判断是否有对应按钮权限
     * @param userId
     * @return
     */
    String getFunCodeByUserId(Integer userId);
}

