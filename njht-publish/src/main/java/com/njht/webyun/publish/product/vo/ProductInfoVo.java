package com.njht.webyun.publish.product.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.njht.webyun.entity.PageEntity;
import com.njht.webyun.utils.DateFormatUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/11 16:49
 * @Description: 产品相关前端传递参数
 */
@ToString
@Data
@ApiModel(value = "产品相关请求参数")
public class ProductInfoVo extends PageEntity {

    @ApiModelProperty(value = "产品id")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "行政区域")
    private String regionId;

    @ApiModelProperty(value = "区域边界")
    private Map geo;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotBlank
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotBlank
    private String endTime;

    @ApiModelProperty(value = "数据源")
    private List<String> dataSourceList;

    @ApiModelProperty(value = "周期")
    private List<String> cycleList;

    @ApiModelProperty(value = "产品是否发布，0未发布 1已发布 2全部")
    private Integer isShow;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = DateFormatUtils.setEndTime(endTime);
    }
}
