package com.njht.entity.xxljob;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author daiguojun
 * @date 2022-08-10 16:15
 * 日志统计结果
 */
@ApiModel
@Data
public class CountInfo {

    @ApiModelProperty(value = "任务总数")
    private Integer tasCount;

    @ApiModelProperty(value = "生产产品总数")
    private Integer productCount;

    @ApiModelProperty(value = "调度总数")
    private Integer dispatchCount;

    @ApiModelProperty(value = "执行器数量")
    private Integer groupCount;
}
