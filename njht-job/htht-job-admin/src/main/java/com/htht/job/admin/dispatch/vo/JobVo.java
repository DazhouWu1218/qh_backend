package com.htht.job.admin.dispatch.vo;

import com.njht.webyun.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "任务传参")
public class JobVo extends PageEntity {

    @ApiModelProperty(value = "任务执行状态,1-执行,0-停止,-1 全部")
    private Integer triggerStatus;

    @ApiModelProperty(value = "产品id")
    @NotNull(message = "产品id不能为空")
    private String productId;

    @ApiModelProperty(value = "任务名称")
    private String jobDesc;

    @ApiModelProperty(value = "任务id, -1 全部")
    private Integer jobId;

    @ApiModelProperty(value = "执行器id")
    private Integer jobGroup;

    @ApiModelProperty(value = "负责人")
    private String author;
}
