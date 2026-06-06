package com.njht.webyun.publish.index.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/11 14:45
 * @Description: 首页产品分类信息
 */
@Data
@ApiModel(value = "IndexProductCategoryReqVo", description = "首页产品分类信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndexProductCategoryReqVo {

    @ApiModelProperty(value = "名称",example = "农业服务")
    private String name;

    @ApiModelProperty(value = "产品id",example = "1")
    private String productId;

}
