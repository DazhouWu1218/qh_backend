package com.htht.job.admin.dispatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * xxl-job log, used to track trigger process
 * @author piesat  2015-12-19 23:19:09
 */
@Data
@ApiModel("日志返回信息")
public class JobLogReqVo {

	@ApiModelProperty(value = "id")
	private long id;
	
	// job info
	@ApiModelProperty(value = "执行器id")
	private int jobGroup;
	@ApiModelProperty(value = "任务id")
	private int jobId;

	private String jobName;

	/**
	 * 产品树结构id
	 */
	@ApiModelProperty(value = "任务目录id")
	private String treeId;

	// execute info
	@ApiModelProperty(value = "任务执行器地址")
	private String executorAddress;

	@ApiModelProperty(value = "失败重试次数")
	private int executorFailRetryCount;
	
	// trigger info
	@ApiModelProperty(value = "调度时间")
	private Date triggerTime;
	@ApiModelProperty(value = "调度结果 200成功 500失败")
	private int triggerCode;
	@ApiModelProperty(value = "调度备注")
	private String triggerMsg;
	
	// handle info
	@ApiModelProperty(value = "执行时间")
	private Date handleTime;
	@ApiModelProperty(value = "调度结果 200成功 201成功(没有数据) 500失败 502失败(超时)")
	private int handleCode;
	@ApiModelProperty(value = "执行备注")
	private String handleMsg;


}
