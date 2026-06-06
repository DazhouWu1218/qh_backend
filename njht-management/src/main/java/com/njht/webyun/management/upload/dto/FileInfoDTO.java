package com.njht.webyun.management.upload.dto;

import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/6/23 15:45
 * @Description: 文件类型实体
 */
@Data
public class FileInfoDTO {
    private String productName;
    private String tifSize;
    private String jpgSize;
    private String xlsSize;
    private String docSize;
}
