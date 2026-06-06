package com.njht.webyun.publish.product.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/11 18:54
 * @Description: 产品文件基本详情
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductFileBaseReqVo {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "文件图片相对路径")
    private String fileUrl;

    private String productInfoId;

    private String zt;
    private String issue;
    private String mark;
    private String cycle;
}
