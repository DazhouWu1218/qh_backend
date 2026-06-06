package com.njht.webyun.management.thematicData.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 实体
 * @author dgj
 * @since 1.0
 *
 */
@Data
public class HthtThematicDataFileInfo  {
	
	private static final long serialVersionUID = 8688083076889710922L;

	private String id;

	/** 专题数据id */
	private String thematicId;

	/** 文件名称 */
	private String fileName;

	/** 文件类型 */
	private String fileType;

	/** 文件路径 */
	private String filePath;

	/** 相对路径 */
	private String fileUrl;

	/** 数据yuan */
	private String dataSource;

	/** 数据类型 */
	private String dataType;

	/** 周期 */
	private String cycle;

	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date createTime;

	/** 修改时间 */
	private Date updateTime;

	private String mark;

	private List<ThematicDataTifInfo> tifInfoList;

}