package com.njht.webyun.publish.sys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;

import com.njht.webyun.publish.sys.dao.RoleUserDao;
import com.njht.webyun.publish.sys.entity.RoleUserEntity;
import com.njht.webyun.publish.sys.service.RoleUserService;


@Service("roleUserService")
public class RoleUserServiceImpl extends ServiceImpl<RoleUserDao, RoleUserEntity> implements RoleUserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RoleUserEntity> page = this.page(
                new Query<RoleUserEntity>().getPage(params),
                new QueryWrapper<RoleUserEntity>()
        );

        return new PageUtils(page);
    }

}