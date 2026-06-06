package com.htht.job.admin.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author 代国军
 * @description: 模板返回结果
 * @date 2022/6/29 10:19
 */
@Data
@ApiModel(value = "模板参数")
public class ParamInfoVo {
    
    @ApiModelProperty(value = "id")
    private String id;

    @NotEmpty(message = "参数标识不能为空")
    @ApiModelProperty(value = "参数标识")
    private String identify;
    @NotEmpty(message = "参数名称不能为空")
    @ApiModelProperty(value = "参数名称")
    private String des;

    @NotEmpty(message = "参数类型不能为空")
    @ApiModelProperty(value = "参数类型")
    private String type;

    @ApiModelProperty(value = "参数当前值")
    private String currentValue;

    @NotEmpty(message = "控件名称不能为空")
    @ApiModelProperty(value = "控件名称")
    private String control;

//    @NotEmpty(message = "控件json不能为空")
    @ApiModelProperty(value = "控件json")
    private String controlStr;

    @ApiModelProperty(value = "排序字段")
    private Integer num;
}
