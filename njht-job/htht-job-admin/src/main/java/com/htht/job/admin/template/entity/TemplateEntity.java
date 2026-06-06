package com.htht.job.admin.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 模板管理表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-06-29 09:14:41
 */
@Data
@TableName("htht_cluster_schedule_template")
public class TemplateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	/**
	 * 版本号
	 */
	@TableField(fill = FieldFill.INSERT)
	private Integer version;
	/**
	 * 模板类型（0调度模板 1算法模板）
	 */
	private Integer identify;
	/**
	 * 父id
	 */
	private String parentId;
	/**
	 * 类型 （0目录 1数据）
	 */
	private String type;
	/**
	 * 模板名称
	 */
	private String name;

}
