package com.njht.webyun.publish.notice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-26 12:24:54
 */
@Data
@TableName("publish_notice_info")
public class NoticeInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createdDate;
	/**
	 * 创建人
	 */
	private Integer createdBy;
	/**
	 * 修改时间 
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date lastUpdatedDate;
	/**
	 * 最后修改人
	 */
	private Integer lastUpdatedBy;
	/**
	 * 公告标题
	 */
	private String title;
	/**
	 * 公告信息
	 */
	private String content;
	/**
	 * 图片相对路径
	 */
	private String imgUrl;
	/**
	 * 图片绝对路径
	 */
	private String imgPath;
	/**
	 * 是否置顶（0 1）
	 */
	private Integer isTop;
	/**
	 * 逻辑删除（0 1）
	 */
	@TableLogic(value = "0",delval = "1")
	private Integer deleted;

	/**
	 * 定时任务时间戳信息
	 */
	private String time;

	/**
	 * 作者
	 */
	private String author;

}
