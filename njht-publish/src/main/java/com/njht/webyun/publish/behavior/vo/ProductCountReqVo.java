package com.njht.webyun.publish.behavior.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/30 14:05
 * @Description: 产品下载量统计
 */
@Data
@ApiModel(value = "产品下载量统计")
public class ProductCountReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "year,mon,week")
    private String mark;
    @ApiModelProperty(value = "x轴信息")
    private List<String> timeList;
    @ApiModelProperty(value = "y轴信息")
    private List<BehaviorProductInfoReqVo> yList;
    @ApiModelProperty(value = "圆环信息")
    private List<BehaviorProductCycleReqVo> cycleList;

}
