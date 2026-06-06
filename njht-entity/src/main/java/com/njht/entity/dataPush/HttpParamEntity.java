package com.njht.entity.dataPush;

/**
 * @author daiguojun
 * @date 2022-09-06 14:53
 * http 请求参数实体类
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HttpParamEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 插入数据的ES类型，使用各系统申请的TYPE
     */
    private String type;

    /**
     * 数据名称
     */
    private String name;

    /**
     * 数据消息
     */
    private String message;

    /**
     * 时间戳
     */
    @JsonProperty(value = "occur_time")
    private Long occurTime;

    /**
     * 数据json
     */
    private Object fields;

    public HttpParamEntity(String type, String name) {
        this.type = type;
        this.name = name;
        occurTime = System.currentTimeMillis();
        message = "";
    }

}
