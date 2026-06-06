package com.njht.entity.dataPush;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author 代国军
 * @description: 数据统计实体
 * @date 2022/9/13 15:22
 */
@Data
public class DataStatusEntity extends PushDataCommonEntity {

    private static final long serialVersionUID = 4565577051699633328L;
    @JsonProperty("data_type")
    private String dataType;

    @JsonProperty("data_cycle")
    private String dataCycle;

    @JsonProperty("data_size")
    private Double dataSize;

    @JsonProperty("data_number")
    private Integer dataNumber;

    @JsonProperty("data_integrity")
    private String dataIntegrity;

    @JsonProperty("data_arrival_rate")
    private String dataArrivalRate;

    @JsonProperty("data_production")
    private String dataProduction;
}
