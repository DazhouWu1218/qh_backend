package com.htht.executor.download.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据下载表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-17 16:41:22
 */
@Data
@TableName("htht_cluster_schedule_download_file_info")
public class DownloadFileInfoEntity implements Serializable {
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
	 * 版本
	 */
	private Integer version;
	/**
	 * 备注
	 */
	private String bz;
	/**
	 * 文件时间
	 */
	private Date dataTime;
	/**
	 * 文件名称
	 */
	private String fileName;
	/**
	 * 文件路径
	 */
	private String filePath;
	/**
	 * 文件大小
	 */
	private Long fileSize;
	/**
	 * 文件名后缀
	 */
	private String format;
	/**
	 * 
	 */
	private String orbiteid;
	/**
	 * 文件名称
	 */
	private String realFileName;
	/**
	 * 状态（1 下载成功）
	 */
	private String zt;

}
