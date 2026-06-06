package com.njht.webyun.management.upload.entity;

import lombok.Data;

/**
 * 文件传输位置的实体类
 * @author zhushizhen
 */
@Data
public class DataUploadTransferFileEntity {
    /**
     * 主键id
     */
    private String id;
    /**
     * 任务的id
     */
    private String jobId;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件的大小
     */

    private Long fileSize;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件位置
     */
    private String fileLocation;
}
