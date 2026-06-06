package com.njht.webyun.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/11 10:55
 * @Description: 分页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页，等于0时默认查第一页")
    private Integer page = 0;

    @ApiModelProperty(value = "每页条数")
    private Integer size = 10;
}
