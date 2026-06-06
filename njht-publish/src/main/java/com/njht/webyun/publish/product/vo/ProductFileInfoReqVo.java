package com.njht.webyun.publish.product.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/11 18:51
 * @Description: 产品文件返回实体对象
 */
@Data
@ApiModel(value = "产品文件详情")
public class ProductFileInfoReqVo {

    @ApiModelProperty(value = "产品说明")
    private String fileDes;

    @ApiModelProperty(value = "文件详情集合")
    private List<ProductFileBaseReqVo> fileInfoList;


}
