package com.njht.webyun.publish.notice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.publish.notice.entity.NoticeUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-26 12:24:54
 */
public interface NoticeUserService extends IService<NoticeUserEntity> {

    /**
     * 查询用户通知信息集合
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取用户未读通知公告信息
     * @param userId
     * @return
     */
    List<String> getUnReadNoticeInfoByUserId(Integer userId);

    /**
     * 通过用户id 删除通知信息
     * @param id
     * @param noticeIds
     */
    void removeByUserIdAndNoticeId(Integer id, List<String> noticeIds);

    /**
     * 根据通知公告的id 查询未读该条通知公告的用户信息
     * @param id
     * @return
     */
    List<Integer> selectUnReadInfoListByNoticeId(String id);

    /**
     * 通过通知公告id 删除
     * @param id
     */
    void removeByNoticeId(String id);

    /**
     * 通过通知公告id 删除
     * @param idList
     */
    void removeByNoticeIds(List<String> idList);
}

