package com.htht.job.admin.plugin.vo;

import com.njht.webyun.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 代国军
 * @description: 列表查询实体类
 * @date 2022/5/31 10:34
 */
@Data
@ApiModel(value = "列表查询实体")
public class HandlerSearchVo extends PageEntity {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "算法名称")
    private String name;

    @ApiModelProperty(value = "算法标识")
    private String identify;
}
