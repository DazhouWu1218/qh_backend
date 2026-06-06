package com.njht.webyun.publish.product.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.entity.PageEntity;
import com.njht.webyun.utils.PageUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author daiguojun
 * @date 2022-08-20 14:22
 * 发布定制页面返回类型
 */
@Data
@ApiModel("农气生态页面查询参数")
public class CategoryProductSearchVo extends PageEntity {

    @ApiModelProperty("id")
    @NotEmpty(message = "id 不能为空")
    private String id;

    @ApiModelProperty("类型 监测，预测")
    private String type;

    @ApiModelProperty("名称 春小麦..")
    private String name;

    @ApiModelProperty("周期")
    @NotEmpty(message = "周期不能为空")
    private String  cycle;

    @ApiModelProperty("行政区域id, 行政区域接口 见高级检索页行政区域")
    private String regionId;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotBlank
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotBlank
    private String endTime;
}
