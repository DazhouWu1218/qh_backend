package com.njht.webyun.publish.sys.dao;

import com.njht.webyun.publish.sys.entity.OrgEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-30 20:05:56
 */
@Mapper
public interface OrgDao extends BaseMapper<OrgEntity> {

    /**
     * 通过用户id 查询对应的行政区域id
     * @param userId
     * @return
     */
    String selectRegionIdByUserId(@Param("userId") Integer userId);
}
