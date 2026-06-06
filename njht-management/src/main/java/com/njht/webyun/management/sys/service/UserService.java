package com.njht.webyun.management.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.management.sys.entity.UserEntity;

/**
 * 系统用户
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-26 09:38:22
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 根据用户id 查询用户真实名称
     * @param userId
     * @return 返回用户真实名称
     */
    String queryRealNameByUserId(Integer userId);

    /**
     * 已注册用户总数
     * @return
     */
    Long countUserNumber();
}

