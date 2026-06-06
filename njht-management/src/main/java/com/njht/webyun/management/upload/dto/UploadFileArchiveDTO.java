package com.njht.webyun.management.upload.dto;

import com.njht.webyun.management.upload.entity.FileInformationEntity;
import lombok.Data;

import java.io.File;
import java.io.Serializable;

/**
 * 文件上传调用支撑平台的工具类
 * @author zhushizhen
 */
@Data
public class UploadFileArchiveDTO implements Serializable {

    private FileInformationEntity fileInformationEntity;

    private File file;

    private String jobId;

}
