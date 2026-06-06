package com.njht.webyun.publish.product.vo;

import com.njht.webyun.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/25 9:52
 * @Description: 产品模糊搜索类
 */
@Data
@ApiModel(value = "模糊搜索类")
public class ProductFuzzySearchVo extends PageEntity {


    @ApiModelProperty(value = "产品id")
    @NotBlank(message = "id不能为空")
    private String productId;

    @ApiModelProperty(value = "数据源")
    private String dataSource;

    @ApiModelProperty(value = "周期")
    private String cycle;
}
