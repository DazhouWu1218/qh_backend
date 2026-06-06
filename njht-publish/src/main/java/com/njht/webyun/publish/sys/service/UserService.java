package com.njht.webyun.publish.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.publish.behavior.vo.CountInfoReqVo;
import com.njht.webyun.publish.sys.entity.UserEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-26 09:38:22
 */
public interface UserService extends IService<UserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 用户信息集合
     * @return
     * @param userIdList
     */
    Map<Integer, UserEntity> getFeedBackUserMap(List<Integer> userIdList);

    /**
     * 根据开始结束时间统计用户注册量
     * @param endTime
     * @param weekStartTime
     * @param monStartTime
     * @param yearStartTime
     * @return
     */
    CountInfoReqVo queryUserRegisterCount(Date endTime, Date weekStartTime, Date monStartTime, Date yearStartTime);

}

