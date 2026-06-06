package com.htht.executor.satellite.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 卫星数据在入库流程中,文件的记录表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-19 11:40:19
 */
@Data
@TableName("htht_dms_sate_data_task_file")
public class SateDataTaskFileEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	/**
	 * 
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	/**
	 * 
	 */
	private Integer version = 0;
	/**
	 * 任务id,流程设计器中对应的task的id
	 */
	private String taskId;
	/**
	 * 磁盘id
	 */
	private String diskId;
	/**
	 * 文件无后缀的名字
	 */
	private String fileRealName;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 文件路径
	 */
	private String filePath;
	/**
	 * 相对路径
	 */
	private String relativePath;
	/**
	 * 是否是文件夹
	 */
	private Integer assertDirectory;
	/**
	 * 文件大小
	 */
	private Long fileSize;
	/**
	 * 文件描述
	 */
	private String description;

}
