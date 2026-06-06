package com.htht.job.admin.dispatch.vo;

import com.njht.webyun.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "任务日志传参")
public class JobLogVo extends PageEntity {

    @ApiModelProperty(value = "任务目录id")
    private String treeId;
    @ApiModelProperty(value = "执行器id")
    private int jobGroup;
    @ApiModelProperty(value = "任务id")
    private int jobId;
    @ApiModelProperty(value = "日志状态 -1全部 1成功 2失败 3.进行中")
    private int logStatus;
    @ApiModelProperty(value = "开始时间")
    private Date triggerTimeStart;
    @ApiModelProperty(value = "结束时间")
    private Date triggerTimeEnd;
}
