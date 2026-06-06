package com.njht.webyun.management.upload.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 与日志表关联的文件类
 * @author zhushizhen
 */
@Data
public class DataUploadLogFileEntity implements Serializable {
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

    private String fileSize;
    /**
     * 文件的大小，以字节为单位
     */
    private Long fileSizeByte;
    /**
     * 文件类型
     */
    private String fileType;

}
