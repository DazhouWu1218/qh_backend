package com.njht.webyun.publish.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.publish.sys.entity.RoleUserEntity;

import java.util.Map;

/**
 * 用户角色表
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-30 20:05:56
 */
public interface RoleUserService extends IService<RoleUserEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

