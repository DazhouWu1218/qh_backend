package com.htht.job.admin.dispatch.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/11 18:35
 * @Description: 常用类型定义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id,唯一")
    private int value;

    @ApiModelProperty(value = "名称")
    private String label;

}
