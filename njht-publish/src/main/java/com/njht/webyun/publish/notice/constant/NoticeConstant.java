package com.njht.webyun.publish.notice.constant;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/26 14:38
 * @Description: 公告相关常量
 */
public class NoticeConstant {

    public static final String NOTICE_IMG_PREFIX = "notice";
    public static final String NOTICE_REDIS_LOCK = "notice:notice_lock";
    public static final String NOTICE_REDIS_INFO = "notice:notice_info";
    public static final String NOTICE_READ_WRITE_LOCK = "notice:notice_read_write_lock";
    public static final String NOTICE_INFO_DB_MSG = "公告发布成功";
    public static final String NOTICE_INFO_REDIS_MSG = "公告将会定时发布";

    public static final String DB_NOTICE_ID = "notice_id";
}
