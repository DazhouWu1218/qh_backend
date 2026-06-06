package com.njht.webyun.publish.notice.dto;

import com.njht.webyun.publish.notice.entity.NoticeInfoEntity;
import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/12/7 10:38
 * @Description: 通知公告信息
 */
@Data
public class NoticeInfoDto extends NoticeInfoEntity {

    private String userName;
}
