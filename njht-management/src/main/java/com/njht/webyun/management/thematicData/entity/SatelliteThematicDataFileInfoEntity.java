package com.njht.webyun.management.thematicData.entity;

import lombok.Data;


/**
 * 实体
 * @author daiguojun
 * @since 1.0
 *
 */
@Data
public class SatelliteThematicDataFileInfoEntity  {

	private String id;
	/** 对应产品id */
	private String dataId;

	/**文件路径  */
	private String filePath;

	/**文件url  */
	private String fileUrl;
	/**  */
	private String fileName;

	/**  */
	private Long fileSize;
	private String fileType;


}