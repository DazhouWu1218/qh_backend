package com.htht.job.admin.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 模板参数表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-06-29 09:14:41
 */
@Data
@TableName("htht_cluster_schedule_template_parameters")
public class TemplateParametersEntity implements Serializable {
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
	 * 模板id
	 */
	private String templateId;
	/**
	 * 参数标识
	 */
	private String identify;
	/**
	 * 参数中文名称
	 */
	private String des;
	/**
	 * 参数类型（string,int,date）
	 */
	private String type;
	/**
	 * 控件名称
	 */
	private String control;
	/**
	 * 选中的值
	 */
	private String currentValue;
	/**
	 * 控件json
	 */
	private String controlJson;

	/**
	 * 排序字段
	 */
	private Integer sortKey;

}
