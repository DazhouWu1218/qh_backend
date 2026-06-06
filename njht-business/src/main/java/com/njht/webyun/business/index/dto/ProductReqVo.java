package com.njht.webyun.business.index.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: 代国军
 * @CreateDate: 2022/1/7 13:59
 * @Description: 产品生产返回信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "产品生产返回信息")
public class ProductReqVo {

    @ApiModelProperty(value = "周期名称")
    private String cycleName;
    @ApiModelProperty(value = "期次")
    private String issue;
    @ApiModelProperty(value = "产品名称")
    private String name;
    @ApiModelProperty(value = "产品生产时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date handleTime;
}
