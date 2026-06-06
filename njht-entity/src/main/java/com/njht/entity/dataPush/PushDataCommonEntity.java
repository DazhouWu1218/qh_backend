package com.njht.entity.dataPush;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author daiguojun
 * @date 2022-09-06 15:00
 * 公共实体
 */
@Data
@AllArgsConstructor
class PushDataCommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录时间
     */
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    @JsonProperty(value = "record_time")
    private Date recordTime;

    /**
     * 统计周期 DAY,MONTH,YEAR
     */
    @JsonProperty(value = "count_cycle")
    private String countCycle;

    PushDataCommonEntity() {
        recordTime = new Date();
        countCycle = "DAY";
    }
}
