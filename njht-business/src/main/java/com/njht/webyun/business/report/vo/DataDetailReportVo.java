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
@ApiModel
public class DataDetailReportVo extends PageEntity {

    @ApiModelProperty("id")
    @NotEmpty(message = "id不能为空")
    private String id;

    @ApiModelProperty("查询周期类型（today weekly monthly，默认 today")
    private String type;

}
