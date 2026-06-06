package com.njht.webyun.publish.behavior.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/12/8 10:26
 * @Description: 下载信息统计结果
 */
@Data
public class BehaviorLogDownLoadReqVo {

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String produceName;

    @ApiModelProperty(value = "下载时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String createdDate;

    @ApiModelProperty(value = "产品时间，期次")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String productTime;
    /**
     *卫星
     */
    @ApiModelProperty(value = "卫星")
    private String satellite;
    /**
     * 周期
     */
    @ApiModelProperty(value = "周期")
    private String cycleName;
    /**
     * 分辨率
     */
    @ApiModelProperty(value = "分辨率")
    private String resolution;

    @ApiModelProperty(value = "产品类型")
    private String productType;

    @ApiModelProperty(value = "区域")
    private String regionName;
}
