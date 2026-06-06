package com.htht.job.admin.plugin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 插件管理表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-30 13:12:01
 */
@Data
@TableName("htht_cluster_schedule_handler")
public class HandlerEntity implements Serializable {
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
	private Integer version = 0;

	/**
	 * 插件handler（对应代码handler）
	 */
	private String modelIdentify;
	/**
	 * 插件名称 菜单名称
	 */
	private String modelName;
	/**
	 * 父id
	 */
	private String parentId;
	/**
	 * 类型 （0目录 1菜单）
	 */
	private String type;
	/**
	 * 模板Id
	 */
	private String templateId;
	/**
	 * 执行器id
	 */
	private Integer groupId;

}
