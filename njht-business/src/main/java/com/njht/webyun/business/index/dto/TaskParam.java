package com.njht.webyun.business.index.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 代国军
 * @CreateDate: 2022/1/7 12:38
 * @Description: 任务监控
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "任务参数信息")
public class TaskParam {

    @ApiModelProperty(value = "任务类型,用作别的接口参数")
    private String type;

    @ApiModelProperty(value = "类型名称")
    private String name;

    @ApiModelProperty(value = "数量")
    private Long value;
}
