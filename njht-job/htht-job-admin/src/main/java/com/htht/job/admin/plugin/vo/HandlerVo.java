package com.htht.job.admin.plugin.vo;

import com.njht.webyun.valid.SaveGroup;
import com.njht.webyun.valid.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;

/**
 * @author 代国军
 * @description: 入库修改，前端传参
 * @date 2022/5/31 9:50
 */
@ApiModel(value = "插件管理前端传参,新增目录只用传name 和 父id，新增数据需要根据情况传参")
@Data
@EqualsAndHashCode
public class HandlerVo {

    @ApiModelProperty(value = "id,修改时传参")
    @Null(message = "新增id必须为空",groups = {SaveGroup.class})
    @NotEmpty(message = "修改id不能为空",groups = {UpdateGroup.class})
    private String id;

    /**
     * 插件handler（对应代码handler）
     */
    @ApiModelProperty(value = "插件handler,页面输入")
    private String modelIdentify;
    /**
     * 插件名称 菜单名称
     */
    @ApiModelProperty(value = "名称，目录名称或者数据名称")
    @NotEmpty(message = "名称不能为空",groups = SaveGroup.class)
    private String modelName;
    /**
     * 父id
     */
    @ApiModelProperty(value = "父id")
    @NotEmpty(message = "父id不能为空,默认根节点父id为0",groups = {SaveGroup.class})
    private String parentId;
    /**
     * 类型 （0目录 1数据）
     */
    @ApiModelProperty(value = "类型 （0目录 1数据）")
    @NotEmpty(message = "类型必选 0目录1数据",groups = {SaveGroup.class})
    private String type;
    /**
     * 对应参数模板路由
     */
    @ApiModelProperty(value = "对应模板Id")
    private String templateId;
    /**
     * 执行器id
     */
    @ApiModelProperty(value = "执行器id")
    private Integer groupId;

}
