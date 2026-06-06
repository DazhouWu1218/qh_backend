package com.njht.webyun.publish.product.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/11 16:25
 * @Description: 产品列表信息
 */
@Data
@ApiModel(value = "ProductInfoReqVo", description = "产品信息列表")
public class ProductInfoReqVo {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "父级名称")
    private String parentName;

    @ApiModelProperty(value = "标识")
    private String mark;

    @ApiModelProperty(value = "发服务期次")
    private String issue;

    @ApiModelProperty(value = "页面展示期次")
    private String period;

    @ApiModelProperty(value = "周期")
    private String cycle;

    @ApiModelProperty(value = "周期名称")
    private String cycleName;

    @ApiModelProperty(value = "数据源")
    private String dataSource;

    @ApiModelProperty(value = "行政区域")
    private String regionId;

    @ApiModelProperty(value = "分辨率")
    private String resolution;

    @ApiModelProperty(value = "文件个数")
    private Integer fileNum;

    @ApiModelProperty(value = "文件相对路径")
    private String fileUrl;

    @ApiModelProperty(value = "缩略图相对路径")
    private String fileThumbUrl;

    @ApiModelProperty(value = "文件类型集合")
    private List<String> fileTypeList;

    @ApiModelProperty(value = "文件详情集合")
    private List<ProductFileBaseReqVo> fileInfoList;

}
