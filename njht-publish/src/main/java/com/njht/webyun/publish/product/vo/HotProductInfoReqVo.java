package com.njht.webyun.publish.product.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/11 16:02
 * @Description: 产品返回信息
 */
@Data
@ApiModel(value = "hotProductInfoReqVo", description = "热门产品返回信息")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HotProductInfoReqVo {
    @ApiModelProperty(value = "产品id",example = "1")
    private String productId;

    @ApiModelProperty(value = "名称",example = "")
    private String productName;

    @ApiModelProperty(value = "产品图片",example = "")
    private String imgUrl;

}
