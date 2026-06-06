package com.njht.webyun.publish.behavior.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njht.webyun.publish.behavior.dto.BehaviorLogDto;
import com.njht.webyun.publish.behavior.entity.BehaviorLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-12 14:55:20
 */
@Mapper
public interface BehaviorLogDao extends BaseMapper<BehaviorLogEntity> {

    /**
     * 查询用户行为日志
     * @param orgId
     * @param roleId
     * @param search
     * @param startTime
     * @param endTime
     * @param set
     * @return
     */
    List<BehaviorLogDto> selectBehaviorLogByPage(@Param("orgId")Integer orgId,
                                                 @Param("roleId")Integer roleId,@Param("search")String search,
                                                 @Param("startTime") String startTime,
                                                 @Param("endTime") String endTime, @Param("actionList") Set<Integer> set);

    /**
     * 通过用户id查询当前用户当天的下载记录
     * @param userId
     * @return
     */
    BehaviorLogEntity selectBehaviorLogByUserIdAndNowDate(@Param("userId") Integer userId);

    /**
     * 查询用户访问总量
     * @return
     */
    Integer selectViewCountInfo();
}
