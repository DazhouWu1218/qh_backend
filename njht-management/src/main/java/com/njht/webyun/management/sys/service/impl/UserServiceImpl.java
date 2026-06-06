package com.njht.webyun.management.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.webyun.management.sys.dao.UserDao;
import com.njht.webyun.management.sys.entity.UserEntity;
import com.njht.webyun.management.sys.service.UserService;
import org.springframework.stereotype.Service;



/**
 * @author daiguojun
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {


    @Override
    public String queryRealNameByUserId(Integer userId) {
        UserEntity userEntity = baseMapper.selectById(userId);
        return userEntity.getRealName();
    }

    @Override
    public Long countUserNumber() {
        return (long) this.count();
    }
}