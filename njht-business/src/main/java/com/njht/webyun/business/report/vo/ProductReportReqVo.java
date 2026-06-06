package com.njht.webyun.business.report.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author daiguojun
 * @date 2022-08-16 16:44
 * 数据统计
 */
@Data
@ApiModel("生产监控 返回结果")
public class ProductReportReqVo extends DataReportReqVo{

    @ApiModelProperty("期次")
    private String issue;

    private String cycle;
    private String cycleName;

    @ApiModelProperty("数据个数")
    private Long fileNum;


}
