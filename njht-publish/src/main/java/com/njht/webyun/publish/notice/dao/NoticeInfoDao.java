package com.njht.webyun.publish.notice.dao;

import com.njht.webyun.publish.notice.dto.NoticeInfoDto;
import com.njht.webyun.publish.notice.entity.NoticeInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-26 12:24:54
 */
@Mapper
public interface NoticeInfoDao extends BaseMapper<NoticeInfoEntity> {

    /**
     * 查询置顶信息
     * @param timeMillis
     * @return
     */
    NoticeInfoDto selectTopNoticeInfo(@Param("timeMillis") Long timeMillis);

    /**
     * 查询公告列表信息，排除置顶信息
     * @param id
     * @param timeMillis
     * @return
     */
    List<NoticeInfoDto> selectNoticeListNoTopId(@Param("id") String id,@Param("timeMillis") Long timeMillis);

    /**
     * 查询最新消息用作指定消息
     * @param timeMillis
     * @return
     */
    NoticeInfoDto selectNewNoticeInfo(@Param("timeMillis")long timeMillis);
}
