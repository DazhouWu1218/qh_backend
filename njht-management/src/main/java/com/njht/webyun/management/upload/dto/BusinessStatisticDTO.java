package com.njht.webyun.management.upload.dto;

import lombok.Data;

import java.util.List;


/**
 * @Author: 代国军
 * @CreateDate: 2021/6/23 15:30
 * @Description: 业务产品生产情况统计分析
 */
@Data
public class BusinessStatisticDTO {
    private String timeRange;
    private String unit;
    private List<FileInfoDTO> fileList;
}
