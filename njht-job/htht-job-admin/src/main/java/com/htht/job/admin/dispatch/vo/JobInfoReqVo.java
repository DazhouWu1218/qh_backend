package com.htht.job.admin.dispatch.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.njht.webyun.valid.SaveGroup;
import com.njht.webyun.valid.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author 代国军
 * @description: 任务返回参数
 * @date 2022/6/21 16:01
 */
@Data
@ApiModel
public class JobInfoReqVo{

    @ApiModelProperty(value = "主键ID")
    @NotNull(message = "修改id不能为空",groups = {UpdateGroup.class})
    private int id;
    @ApiModelProperty(value = "任务生产名称")
    @NotEmpty(message = "请输入任务描述",groups = {SaveGroup.class})
    private String jobDesc;

    @ApiModelProperty(value = "调度类型")
    @NotEmpty(message = "调度类型不能为空",groups = {SaveGroup.class})
    private String scheduleType;

    @ApiModelProperty(value = "下次执行时间,（没有下次执行时间返回null）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String nextTriggerTime;

    @ApiModelProperty(value = "调度状态：0-停止，1-运行")
    private int triggerStatus;

    @ApiModelProperty(value = "负责人")
    @NotEmpty(message = "请输入负责人",groups = {SaveGroup.class})
    private String author;

    @ApiModelProperty("产品树结构id")
    @NotEmpty(message = "treeId不能为空",groups = {SaveGroup.class})
    private String treeId;

    @ApiModelProperty(value = "执行器主键ID")
    @NotNull(message = "执行器id不能为空",groups = {SaveGroup.class})
    private int jobGroup;



}
