package com.njht.webyun.publish.notice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.webyun.publish.notice.constant.NoticeConstant;
import com.njht.webyun.publish.notice.dao.NoticeUserDao;
import com.njht.webyun.publish.notice.entity.NoticeUserEntity;
import com.njht.webyun.publish.notice.service.NoticeUserService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通知公告信息
 */
@Service("noticeUserService")
@Transactional(rollbackFor = Exception.class)
public class NoticeUserServiceImpl extends ServiceImpl<NoticeUserDao, NoticeUserEntity> implements NoticeUserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<NoticeUserEntity> page = this.page(
                new Query<NoticeUserEntity>().getPage(params),
                new QueryWrapper<NoticeUserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<String> getUnReadNoticeInfoByUserId(Integer userId) {
        QueryWrapper<NoticeUserEntity> qw = new QueryWrapper<>();
        qw.eq("user_id",userId);
        List<NoticeUserEntity> list = this.list(qw);
        return list.stream().map(NoticeUserEntity::getNoticeId).collect(Collectors.toList());
    }

    @Override
    public void removeByUserIdAndNoticeId(Integer id, List<String> noticeIds) {
        if(id != null &&id != 0 && !CollectionUtils.isEmpty(noticeIds)){
            QueryWrapper<NoticeUserEntity> qw = new QueryWrapper<>();
            qw.eq("user_id",id);
            qw.in(NoticeConstant.DB_NOTICE_ID,noticeIds);
            List<NoticeUserEntity> list = this.list(qw);
            //删除中间表信息
            this.removeByIds(list.stream().map(NoticeUserEntity::getId).collect(Collectors.toList()));
        }
    }

    @Override
    public List<Integer> selectUnReadInfoListByNoticeId(String id) {
        QueryWrapper<NoticeUserEntity> qw = new QueryWrapper<>();
        qw.eq(NoticeConstant.DB_NOTICE_ID,id);
        List<NoticeUserEntity> list = this.list(qw);
        return list.stream().map(NoticeUserEntity::getUserId).collect(Collectors.toList());
    }

    @Override
    public void removeByNoticeId(String id) {
        QueryWrapper<NoticeUserEntity> qw = new QueryWrapper<>();
        qw.eq(NoticeConstant.DB_NOTICE_ID,id);
        List<NoticeUserEntity> list = this.list(qw);
        //删除中间表信息
        this.removeByIds(list.stream().map(NoticeUserEntity::getId).collect(Collectors.toList()));
    }

    @Override
    public void removeByNoticeIds(List<String> idList) {
        QueryWrapper<NoticeUserEntity> qw = new QueryWrapper<>();
        qw.in(NoticeConstant.DB_NOTICE_ID,idList);
        List<NoticeUserEntity> list = this.list(qw);
        //删除中间表信息
        this.removeByIds(list.stream().map(NoticeUserEntity::getId).collect(Collectors.toList()));
    }


}