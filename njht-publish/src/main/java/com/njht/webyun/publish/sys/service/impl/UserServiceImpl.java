package com.njht.webyun.publish.sys.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.webyun.constant.DbConstant;
import com.njht.webyun.publish.behavior.constant.BehaviorConstant;
import com.njht.webyun.publish.behavior.vo.CountInfoReqVo;
import com.njht.webyun.publish.sys.dao.UserDao;
import com.njht.webyun.publish.sys.entity.UserEntity;
import com.njht.webyun.publish.sys.service.UserService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("userService")
@DS(value = DbConstant.MYSQL)
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Map<Integer, UserEntity> getFeedBackUserMap(List<Integer> userIdList) {
        Map<Integer, UserEntity> returnMap = new HashMap<>(16);
        List<UserEntity> userEntities = this.listByIds(userIdList);
        Map<Integer, List<UserEntity>> map = userEntities.stream().collect(Collectors.groupingBy(UserEntity::getUserId));
        map.forEach((key, value) -> returnMap.put(key,value.get(0)));
        return returnMap;
    }

    @Override
    public CountInfoReqVo queryUserRegisterCount(Date endTime, Date weekStartTime, Date monStartTime, Date yearStartTime) {
        return CountInfoReqVo.builder().type(BehaviorConstant.TWO)
                .name(BehaviorConstant.ACTION_TYPE.get(BehaviorConstant.TWO))
                .build();
    }
}