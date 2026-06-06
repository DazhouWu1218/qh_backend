package com.njht.webyun.publish.product.vo;

import com.njht.webyun.entity.CommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author daiguojun
 * @date 2022-08-20 14:22
 * 发布定制页面返回类型
 */
@Data
@ApiModel("农气生态页面返回类型")
public class CategoryProductReqVo {

    private String id;

    @ApiModelProperty("类型集合,检测，预测")
    private List<String> typeList;

    @ApiModelProperty("名称集合 春小麦..")
    private List<String> nameList;

    @ApiModelProperty("周期集合")
    private List<CommonEntity> cycleList;
}
