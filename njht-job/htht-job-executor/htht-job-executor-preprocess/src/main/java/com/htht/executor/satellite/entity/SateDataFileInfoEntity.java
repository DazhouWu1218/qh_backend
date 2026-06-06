package com.htht.executor.satellite.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-19 11:40:19
 */
@Data
@TableName("htht_dms_sate_data_file_info")
public class SateDataFileInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date updateTime;
	/**
	 * 乐观锁
	 */
	private String version = "0";
	/**
	 * 对应的xml元数据id
	 */
	private String dataId;
	/**
	 * 原始文件id
	 */
	private String sourceFileId;
	/**
	 * 归档文件id
	 */
	private String archiveFileId;
	/**
	 * tif文件绝对路径
	 */
	private String tifFilePath;
	/**
	 * tif文件在服务器中的路径(相对路径)
	 */
	private String tifFileUrl;
	/**
	 * tif文件名
	 */
	private String tifFileName;
	/**
	 * tif文件大小 单位为字节
	 */
	private Long tifFileSize;
	/**
	 * 缩略图文件绝对路径
	 */
	private String thumbnailFilePath;
	/**
	 * 缩略图文件在服务器中的路径(相对路径)
	 */
	private String thumbnailFileUrl;
	/**
	 * 缩略图文件名
	 */
	private String thumbnailFileName;
	/**
	 * 缩略图文件大小 单位为字节
	 */
	private Long thumbnailFileSize;
	/**
	 * 拇指文件绝对路径
	 */
	private String thumbChartFilePath;
	/**
	 * 拇指文件在服务器中的路径(相对路径)
	 */
	private String thumbChartFileUrl;
	/**
	 * 拇指文件名
	 */
	private String thumbChartFileName;
	/**
	 * 拇指文件大小 单位为字节
	 */
	private Long thumbChartFileSize;

}
