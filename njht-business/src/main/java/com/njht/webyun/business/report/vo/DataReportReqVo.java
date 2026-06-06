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
@ApiModel
public class DataReportReqVo {


    private String id;
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("到报率/完整性")
    private String rate;
}
