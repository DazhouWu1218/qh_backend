package com.njht.webyun.management.upload.entity;

import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/6/23 16:10
 * @Description: 业务产品统计分析查询结果
 */
@Data
public class BusinessStatisticEntity {
    private String fileType;
    private String issue;
    private String fileSize;
    private String treeId;
}
