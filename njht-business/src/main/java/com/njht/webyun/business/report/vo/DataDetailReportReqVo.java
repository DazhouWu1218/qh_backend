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
public class DataDetailReportReqVo extends DataReportReqVo {

    @ApiModelProperty("期次")
    private String issue;

    @ApiModelProperty("数据大小")
    private String fileSize;

    @ApiModelProperty("数据个数")
    private Long fileNum;

    @ApiModelProperty("生产状态")
    private String status;

    @ApiModelProperty("状态名称")
    private String  statusName;

}
