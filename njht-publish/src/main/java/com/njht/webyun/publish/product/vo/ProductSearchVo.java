package com.njht.webyun.publish.product.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/11 17:21
 * @Description: 产品检索实体
 */
@Data
@ApiModel(value = "高级检索请求信息")
public class ProductSearchVo {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "数据源集合")
    private List<String> dataSourceList;

    @ApiModelProperty(value = "周期")
    private List<ProductCycleReqVo> cycleList;


}
