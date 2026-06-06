package com.njht.webyun.management.business.entity;

/**
 * 实体
 * @author dgj
 * @since 1.0
 *
 */
public class ProductFileInfo {
	
	private static final long serialVersionUID = -710849983557978870L;

	/** 主键id */
	private String id;

	/** 绝对路径 */
	private String relativePath;

	/** 文件类型 */
	private String fileType;

	/** 文件路径*/
	private String filePath;

	/** 文件名 */
	private String fileName;

	/** */
	private String isDel;

	public ProductFileInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}