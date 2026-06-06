package com.njht.webyun.management.thematicData.entity;

import lombok.Data;

/**
 * @author zhushizhen
 */
@Data
public class SatelliteThematicFileDataInfoDTO {
    /**
     * 主键id
     */
    private String id;

    /**
     * 文件名称
     */
    private String fileName;

    private Long fileSize;

    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件相对路径
     */
    private String fileUrl;

    private String  fileTypeName;


}
