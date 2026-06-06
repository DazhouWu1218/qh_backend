package com.njht.webyun.publish.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.webyun.publish.sys.dao.RoleDao;
import com.njht.webyun.publish.sys.entity.RoleEntity;
import com.njht.webyun.publish.sys.service.RoleService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDao, RoleEntity> implements RoleService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RoleEntity> page = this.page(
                new Query<RoleEntity>().getPage(params),
                new QueryWrapper<RoleEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public String getRoleNameByUserId(Integer userId) {
        return baseMapper.selectRoleNameByUserId(userId);
    }

    @Override
    public String getFunCodeByUserId(Integer userId) {
        return baseMapper.selectFunCodeByUserId(userId);
    }

}