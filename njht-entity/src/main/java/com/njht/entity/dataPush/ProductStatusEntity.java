package com.njht.entity.dataPush;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author 代国军
 * @description: 产品统计实体
 * @date 2022/9/13 15:22
 */
@Data
public class ProductStatusEntity extends PushDataCommonEntity {

    private static final long serialVersionUID = 1847281001212345510L;
    @JsonProperty("product_type")
    private String productType;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_cycle")
    private String productCycle;

    @JsonProperty("product_period")
    private String productPeriod;

    @JsonProperty("data_number")
    private Integer dataNumber;

    @JsonProperty("data_integrity")
    private String dataIntegrity;

}
