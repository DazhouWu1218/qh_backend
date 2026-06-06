package com.njht.webyun.management.upload.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhushizhen
 */
@Data
public class DataUploadLogStatusEntity implements Serializable {
    /**
     * 主键id
     */
    private String id;

    /**
     * 文件上传流程的状态
     */
    private Integer version;


    /**
     * 文件上传的流程对应的状态
     */
    private String versionStatus;

    /**
     * 文件上传出现错误信息
     */
    private String errorMessage;

    /**
     * 对应任务的id
     */
    private String jobId;
}
