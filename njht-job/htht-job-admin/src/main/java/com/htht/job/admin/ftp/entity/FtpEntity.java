package com.htht.job.admin.ftp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 调度平台-FTP配置信息表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-06-27 17:05:24
 */
@Data
@TableName("htht_cluster_schedule_ftp")
public class FtpEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	/**
	 * 
	 */
	@TableField(value = "create_time",fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 
	 */
	@TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	/**
	 * 
	 */
	private Integer version = 0;

	@TableLogic(value = "0",delval = "1")
	private Integer isDel = 0;
	/**
	 * ip地址
	 */
	private String ipAddr;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 路径
	 */
	private String path;
	/**
	 * 端口号
	 */
	private Integer port;
	/**
	 * ftp密码
	 */
	private String pwd;
	/**
	 * 用户名
	 */
	private String userName;

}
