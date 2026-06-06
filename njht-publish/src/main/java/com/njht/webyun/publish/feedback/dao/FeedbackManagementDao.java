package com.njht.webyun.publish.feedback.dao;

import com.njht.webyun.publish.feedback.dto.FeedBackDto;
import com.njht.webyun.publish.feedback.entity.FeedbackManagementEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-12 14:50:22
 */
@Mapper
public interface FeedbackManagementDao extends BaseMapper<FeedbackManagementEntity> {

    /**
     * 查询用户反馈信息
     * @param list
     * @return
     */
    List<FeedBackDto> selectFeedBackList(@Param("idList")List<String> list);

    /**
     * 根据用户反馈id 查询图片url
     * @param id
     * @return
     */
    List<String> selectImgUrlListById(@Param("id")String id);

    /**
     * 多条件查询用户反馈信息
     * @param replyStatus
     * @param userId
     * @param parentsId
     * @param startTime
     * @param endTime
     * @param queryInfo
     * @return
     */
    List<FeedBackDto> selectFeedBackListByQueryInfo(@Param("replyStatus")Integer replyStatus,
                                                    @Param("userId")Integer userId,
                                                    @Param("parentsId")Integer parentsId,
                                                    @Param("startTime")String startTime,
                                                    @Param("endTime")String endTime,
                                                    @Param("queryInfo")String queryInfo);
}
