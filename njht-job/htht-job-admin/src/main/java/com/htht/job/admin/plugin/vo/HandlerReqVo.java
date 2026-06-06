package com.htht.job.admin.plugin.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author 代国军
 * @description: 入库修改，前端传参
 * @date 2022/5/31 9:50
 */
@ApiModel(value = "插件管理信息列表")
@Data
public class HandlerReqVo {

    private String id;
    /**
     * 插件handler（对应代码handler）
     */
    private String modelIdentify;
    /**
     * 插件名称 菜单名称
     */
    private String modelName;
    /**
     * 父id
     */
    private String parentId;
    /**
     * 类型 （0目录 1菜单）
     */
    private String type;

    /**
     * 执行器id
     */
    private Integer groupId;

    private String groupName;

    private String templateId;

    private String templateName;
}
