package com.htht.job.admin.dispatch.core.model;

import lombok.Data;

/**
 * @author 代国军
 * @description: 扩展类
 * @date 2022/5/12 20:40
 */
@Data
public class JobInfoDto extends XxlJobInfo{
    /**
     * 动态参数
     */
    private String dynamicParameter;
    /**
     * 固定参数
     */
    private String fixedParameter;

    /**
     * 任务参数模板
     */
    private String modelParameters;

    /**
     * 产品id
     */
    private String productId;

}
