package com.njht.webyun.publish.feedback.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-12 14:50:22
 */
@Data
@TableName("publish_feedback_management")
public class FeedbackManagementEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 问题反馈的id，主键
	 */
	@TableId(type = IdType.AUTO)
	private Integer feedbackId;
	/**
	 * 问题分类
	 */
	private String feedbackType;
	/**
	 * 问题内容
	 */
	private String feedbackContent;
	/**
	 * 是否是原始问题1-是，0-不是  1原始问题， 0 回复
	 */
	private Integer question;
	/**
	 * 父节点id
	 */
	private Integer parentsId;
	/**
	 * 是否公开，1-公开，0-不公开 （不公开只有管理员可见）
	 */
	private Integer publicStatus;
	/**
	 * 回复状态。1-已回复，0-未回复。只针对问题，回复不需要该状态
	 */
	private Integer replyStatus;

	/**
	 * 是否逻辑删除 1-删除，0-不删除
	 */
	@TableLogic(value = "0",delval = "1")
	private Integer deleted;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createdDate;
	/**
	 * 创建者
	 */
	private Integer createdBy;
	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date lastUpdatedDate;
	/**
	 * 最后修改人
	 */
	private Integer lastUpdatedBy;

	private String regionId;

	private Integer toUserId;



}
