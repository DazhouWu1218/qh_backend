package com.njht.webyun.business.index.dto;

import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2022/1/7 12:43
 * @Description: 任务监控
 */
@Data
public class TaskMonitorDto {
    private Long successNum;
    private Long failNum;
    private Long runNum;
}
