package com.njht.webyun.management.order.entity;

import java.util.Date;

/**
 * 实体
 * @author dgj
 * @since 1.0
 *
 */
public class OrderFileInfo {
	
	private static final long serialVersionUID = -8436744778761075552L;

	private String id;

	/** 创建时间 */
	private Date createTime;

	/** 修改时间 */
	private Date updateTime;

	/** 文件路径 */
	private String filePath;

	/** 文件名称 */
	private String fileName;

	/** 文件大小 */
	private String fileSize;

	/** 保存天数 */
	private String saveDay = "30";

	private Integer sysCategoryId;

	public OrderFileInfo() {
	}

	public OrderFileInfo(String id, Date createTime, Date updateTime, String filePath, String fileName, String fileSize,Integer sysCategoryId) {
		this.id = id;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.filePath = filePath;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.sysCategoryId = sysCategoryId;
	}

	public Integer getSysCategoryId() {
		return sysCategoryId;
	}

	public OrderFileInfo setSysCategoryId(Integer sysCategoryId) {
		this.sysCategoryId = sysCategoryId;
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getSaveDay() {
		return this.saveDay;
	}

	public void setSaveDay(String saveDay) {
		this.saveDay = saveDay;
	}

}