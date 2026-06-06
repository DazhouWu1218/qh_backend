package com.njht.webyun.management.thematicData.entity;

import lombok.Data;

import java.util.List;


/**
 * 实体
 * @author daiguojun
 * @since 1.0
 *
 */
@Data
public class SatelliteThematicDataInfoEntity {

	private String id;
	/**文件路径  */
	private String filePath;
	private String fileName;
	private Long fileSize;
	private String fileType;
	private String issue;
	private String  cycle;

	/** 产品标识 */
	private String mark;

	private List<SatelliteThematicDataFileInfoEntity> dateFileInfoList;
}