package com.htht.executor.download.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 支撑平台-FTP配置信息表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-17 16:41:22
 */
@Data
@TableName("htht_cluster_schedule_ftp")
public class FtpEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String id;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 
	 */
	private Integer version;
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
