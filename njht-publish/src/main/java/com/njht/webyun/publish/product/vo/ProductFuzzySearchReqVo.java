package com.njht.webyun.publish.product.vo;

import com.njht.webyun.entity.CommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/25 9:52
 * @Description: 产品模糊搜索类
 * value:  随机生成 唯一 uuid
 * label：  产品名称_模式_数据源_周期_区域
 * productId 表中的产品id
 */
@Data
@ApiModel(value = "模糊搜索结果类")
public class ProductFuzzySearchReqVo extends CommonEntity {


    @ApiModelProperty(value = "产品id")
    private String productId;

    @ApiModelProperty(value = "数据源")
    private String dataSource;

    @ApiModelProperty(value = "周期")
    private String cycle;
}
