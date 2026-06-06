package com.njht.webyun.publish.sys.dao;

import com.njht.webyun.publish.sys.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

/**
 * 系统用户
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-26 09:38:22
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

    /**
     * 查询统计结果
     * @param endTime
     * @param weekStartTime
     * @param monStartTime
     * @param yearStartTime
     * @return
     */
    Map<String, Long> selectCountInfo(@Param("endTime")Date endTime,
                                         @Param("weekStartTime")Date weekStartTime,
                                         @Param("monStartTime")Date monStartTime,
                                         @Param("yearStartTime")Date yearStartTime);


}
