package com.htht.job.core.biz.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-10 10:15:42
 */
@Data
public class TaskParametersEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	private String id;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 版本号
	 */
	private Integer version;
	/**
	 * 动态参数
	 */
	private String dynamicParameter;
	/**
	 * 固定参数
	 */
	private String fixedParameter;
	/**
	 * 任务id
	 */
	private Integer jobId;
	/**
	 * 任务参数模板
	 */
	private String modelParameters;

	/**
	 * 产品id
	 */
	private String productId;

	private String cycle;
	private String satellite;
	private String sensor;

}
