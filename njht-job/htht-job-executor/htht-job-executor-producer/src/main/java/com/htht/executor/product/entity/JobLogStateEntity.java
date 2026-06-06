package com.htht.executor.product.entity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @author chenzedong
 * @email chenzedong@piesat.cn
 * @date 2022-05-26 14:11:02
 */
@Data
@TableName("htht_cluster_schedule_job_log")
public class JobLogStateEntity extends BaseEntity{
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private Long jobId;
	/**
	 * 日志id
	 */
	private Long logId;
	/**
	 * 执行状态(0执行中 1执行结束)
	 */
	private Integer status;
	/**
	 * 状态信息
	 */
	private String msg;
	/**
	 * 时间戳信息
	 */
	@TableField(fill = FieldFill.INSERT)
	private Long time;

}
