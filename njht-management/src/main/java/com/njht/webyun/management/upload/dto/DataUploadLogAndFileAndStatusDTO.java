package com.njht.webyun.management.upload.dto;


import com.njht.webyun.management.upload.entity.DataUploadLogFileEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 文件上传的
 * @author zhushizhen
 */
@Data
public class DataUploadLogAndFileAndStatusDTO {
    /**
     * 主键id
     */
    private String id;
    /**
     * 任务编号
     */
    private String jobNumber;


    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 数据名称
     */
    private String dataName;


    /**
     * 用户的名称
     */
    private String userName;

    /**
     * 文件上传的状态
     */
    private String status;
    /**
     * 文件上传的文件
     */
    private List<DataUploadLogFileEntity> list;

    /**
     * 文件上传的状态
     */
    private Map<String,Object> mapStatus;
}
