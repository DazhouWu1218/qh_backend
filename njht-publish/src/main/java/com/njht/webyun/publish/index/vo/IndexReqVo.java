package com.njht.webyun.publish.index.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/12 10:11
 * @Description: 首页返回信息
 */
@Data
@ApiModel(value = "",description="首页返回信息")
public class IndexReqVo {

    @ApiModelProperty(value = "首页基本信息")
    private IndexBaseReqVo baseInfo;

    @ApiModelProperty(value = "首页产品分类信息")
    private List<IndexProductCategoryReqVo> categoryInfo;

    @ApiModelProperty(value = "首页轮播图")
    private List<IndexProductReqVo> productInfo;

    @ApiModelProperty(value = "业务产品个数",example = "1")
    private Integer businessProductNum;

    @ApiModelProperty(value = "服务单位格式",example = "2")
    private Integer serviceUnitNum;

    @ApiModelProperty(value = "注册用户个数",example = "4")
    private Integer registerUserNum;

}
