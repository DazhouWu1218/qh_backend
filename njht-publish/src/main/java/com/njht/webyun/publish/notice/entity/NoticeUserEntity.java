package com.njht.webyun.publish.notice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-26 12:24:54
 */
@Data
@TableName("publish_notice_user")
public class NoticeUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	/**
	 * 发布公告id
	 */
	private String noticeId;
	/**
	 * 用户id
	 */
	private Integer userId;

}
