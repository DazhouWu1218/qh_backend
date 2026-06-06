package com.njht.webyun.publish.product.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/12 11:52
 * @Description: 专题上格栅数据返回类
 */
@Data
@ApiModel(value = "专题栅格数据返回类")
@AllArgsConstructor
@NoArgsConstructor
public class ImgInfoReqVo {

    @ApiModelProperty(value = "产品类型")
    private String type;

    @ApiModelProperty(value = "图片集合")
    private List<?> info;
}
