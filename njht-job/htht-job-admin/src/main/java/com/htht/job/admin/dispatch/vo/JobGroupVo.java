package com.htht.job.admin.dispatch.vo;

import com.njht.webyun.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 代国军
 * @description: 执行器传参
 * @date 2022/5/30 15:50
 */
@Data
@ApiModel(value = "执行器传参")
public class JobGroupVo extends PageEntity {

    @ApiModelProperty(value = "执行器编码")
    private String appname;

    @ApiModelProperty(value = "执行器名称")
    private String title;

}
