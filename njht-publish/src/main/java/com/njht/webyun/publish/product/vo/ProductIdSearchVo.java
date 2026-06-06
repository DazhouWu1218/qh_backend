package com.njht.webyun.publish.product.vo;

import com.njht.webyun.entity.PageEntity;
import com.njht.webyun.utils.DateFormatUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Author: 代国军
 * @CreateDate: 2021/12/2 19:26
 * @Description: 根据id检索
 */
@Data
@ApiModel(value = "产品id相关参数")
public class ProductIdSearchVo extends PageEntity {

    @NotNull
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "区域边界")
    private Map geo;
    @ApiModelProperty(value = "行政区域id")
    private String regionId;
    @ApiModelProperty(value = "产品是否发布，0未发布 1已发布 2全部")
    private Integer isShow;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = DateFormatUtils.setEndTime(endTime);
    }

}
