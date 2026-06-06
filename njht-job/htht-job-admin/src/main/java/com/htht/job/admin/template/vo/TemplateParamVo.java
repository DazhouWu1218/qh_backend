package com.htht.job.admin.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author 代国军
 * @description: 模板返回结果
 * @date 2022/6/29 10:19
 */
@Data
@ApiModel(value = "模板新增修改传参")
public class TemplateParamVo {

    @ApiModelProperty("模板id")
    private String id;

    @ApiModelProperty(value = "模板名称")
    @NotEmpty(message = "新增模板名称不能为空")
    private String name;

    @ApiModelProperty(value = "父id")
    @NotEmpty(message = "父id 不能为空")
    private String parentId;

    @ApiModelProperty(value = "模板标识 0调度模板 1算法模板",example = "0")
    private Integer identify;

    @ApiModelProperty(value = "参数集合")
    @Valid
    private List<ParamInfoVo> paramList;


}
