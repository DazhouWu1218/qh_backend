package com.njht.webyun.business.report.vo;

import com.njht.webyun.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author daiguojun
 * @date 2022-08-16 16:44
 * 数据统计
 */
@Data
@ApiModel("生产监控参数")
public class ProductReportVo extends PageEntity {

    @ApiModelProperty("状态 0 未生产,1以生产")
    @NotEmpty(message = "标识不能为空")
    private String status;

    @ApiModelProperty("产品id")
    private String productId;

    @ApiModelProperty("预处理 preprocess 产品 product")
    @NotEmpty(message = "类型不能为空")
    private String type;

}
