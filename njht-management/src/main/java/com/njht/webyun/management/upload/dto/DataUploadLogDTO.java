package com.njht.webyun.management.upload.dto;


import com.njht.webyun.management.upload.entity.DataUploadLogStatusEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 返回给前端的文件上传日志
 * @author zhushizhen
 */
@Data
public class DataUploadLogDTO implements Serializable {
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
    private String status;
    /**
     * 文件上传的状态
     */
    private List<DataUploadLogStatusEntity> list;


}
