package com.njht.webyun.publish.index.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/11 14:20
 * @Description: 首页轮播图产品信息
 */
@Data
@ApiModel(value = "IndexProductReqVo", description = "首页轮播图产品信息")
public class IndexProductReqVo {

    @ApiModelProperty(value = "标题",example = "牧草气象条件评价产品全新上线")
    private String title;

    @ApiModelProperty(value = "产品图片集合",example = "牧草气象条件评价产品全新上线")
    private List<String> productImgUrl;

    @ApiModelProperty(value = "对应产品id，用于了解详情按钮",example = "uuid")
    private String productId;

}
