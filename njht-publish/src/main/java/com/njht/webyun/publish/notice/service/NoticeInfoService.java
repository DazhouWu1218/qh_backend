package com.njht.webyun.publish.notice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.publish.notice.entity.NoticeInfoEntity;
import com.njht.webyun.publish.notice.vo.NoticeInfoSearchVo;
import com.njht.webyun.utils.ReturnT;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-26 12:24:54
 */
public interface NoticeInfoService extends IService<NoticeInfoEntity> {

    /**
     * 列表查询
     * @param params
     * @return
     */
    Map<String,Object> queryPage(NoticeInfoSearchVo params);

    /**
     * 保存公告信息
     * @param id
     * @param title
     * @param content
     * @param isTop
     * @param time
     * @param file
     * @param author
     * @return
     */
    ReturnT saveOrUpdateNoticeInfo(String id,String title, String content, Integer isTop, Date time,
                                   MultipartFile file,String author);

    /**
     * 将消息置顶
     * @param id
     * @return
     */
    boolean toTop(String id);

    /**
     * 删除通知公告信息
     * @param asList
     */
    void removeNoticeByIds(List<String> asList);

    /**
     * 定时任务消息入库
     */
    void saveNoticeFromRedisToDb();
}

