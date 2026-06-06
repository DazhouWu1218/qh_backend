package com.njht.webyun.publish.behavior.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/30 10:42
 * @Description: 统计量
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "信息统计")
public class CountInfoReqVo {

    @ApiModelProperty(value = "行为")
    private Integer type;
    @ApiModelProperty(value = "行为名称")
    private String  name;

    @ApiModelProperty(value = "统计结果")
    private Integer count;


}
