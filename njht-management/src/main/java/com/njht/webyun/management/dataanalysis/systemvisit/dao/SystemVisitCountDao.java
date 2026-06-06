package com.njht.webyun.management.dataanalysis.systemvisit.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njht.webyun.management.dataanalysis.systemvisit.dto.SystemVisitDateCountDTO;
import com.njht.webyun.management.dataanalysis.systemvisit.entity.SystemVisitCountEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface SystemVisitCountDao extends BaseMapper<SystemVisitCountEntity> {


    /**
     * 添加访问次数
     */
    void incrementCount();


    /**
     * 根据用户id查询访问次数
     *
     * @param userId
     * @return
     */
    @Select("select * from htht_uus_log_sys_visit_count where user_id=#{userId} and date=#{date}")
    SystemVisitCountEntity selectByUserIdAndDateTime(@Param("userId") String userId, @Param("date") Date date);


    @Insert("insert into htht_uus_log_sys_visit_count(count,last_visit_time,user_id,date,create_time,update_time) values (#{count},#{lastVisitTime},#{userId},#{date},now(),now())")
    void insertNew(@Param("count") Long count, @Param("lastVisitTime") Date lastVisitTime, @Param("userId") String userId, @Param("date") Date date);

    /**
     * 获取访问总量
     */
    @Select("select sum(count) from htht_uus_log_sys_visit_count")
    Long selectAllCount();


    /**
     * 获取访问总量
     *
     * @param userId
     * @return
     */
    @Select("select count(*) from htht_uus_log_sys_visit_count where user_id=#{userId}")
    Long selectUserVisitCount(@Param("userId") String userId);

    @Select("select date,sum(count) count from htht_uus_log_sys_visit_count where date between #{startDay} and #{endDay} group by date order by date asc")
    List<SystemVisitDateCountDTO> selectVisitCountByDay(@Param("startDay") Date startDay, @Param("endDay") Date endDay);


}
