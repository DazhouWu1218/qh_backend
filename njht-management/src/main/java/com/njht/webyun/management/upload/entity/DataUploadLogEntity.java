package com.njht.webyun.management.upload.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件上传的日志类
 * @author zhushizhen
 */
@Data

public class DataUploadLogEntity implements Serializable {
    /**
     * 主键id
     */
    private String id;
    /**
     * 任务编号
     */
    private String jobNumber;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 数据名称
     */
    private String dataName;

    /**
     * 用户的id
     */
    private String userId;
    /**
     * 用户的名称
     */
    private String userName;
    /**
     * 文件的个数
     */
    private Integer fileNumber;

    /**
     * 文件的大小
     */
    private String fileSize;


    /**
     * 文件上传的状态
     */
    private String logStatus;
    /**
     * 对应树状结构的id
     */
    private String treeId;
    /**
     * 文件的大小以字节为单位
     */
    private Long fileSizeByte;

    /**
     * 数据验证判断
     */
    private boolean dataJudge;


}
