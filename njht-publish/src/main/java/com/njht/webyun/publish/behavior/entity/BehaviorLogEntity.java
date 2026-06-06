package com.njht.webyun.publish.behavior.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-12-08 09:09:54
 */
@Data
@TableName("publish_behavior_log")
public class BehaviorLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 行为日志id
	 */
	@TableId(type = IdType.AUTO)
	private Integer behaviorId;
	/**
	 * 动作行为 0-用户访问量，1-数据下载量，2-用户修改
	 */
	private Integer action;
	/**
	 * 是否删除（0-未删除，1-已删除）
	 */
	private Integer deleted;
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
	 * 最后修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date lastUpdatedDate;
	/**
	 * 最后修改人
	 */
	private Integer lastUpdatedBy;

	/**
	 * 下载或者访问次数
	 */
	private Integer num;

}
