package com.njht.webyun.publish.feedback.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-12 14:50:22
 */
@Data
@TableName("publish_feedback_imgurl")
public class FeedbackImgurlEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 反馈id
	 */
	private Integer feedbackId;
	/**
	 * 图片地址
	 */
	private String imgurl;
	/**
	 * 展示序号
	 */
	private Integer sort;

}
