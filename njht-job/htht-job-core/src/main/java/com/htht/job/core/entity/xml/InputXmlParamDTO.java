package com.htht.job.core.entity.xml;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * 输入xml参数
 */
@Data
@ToString
public class InputXmlParamDTO {
    /**
     * 产品周期
     */
    private String issue;

    /**
     * 周期类别
     */
    private String cycle;

    /**
     * 区域ID
     */
    private String areaID;

    /**
     * 输入文件路径
     */
    private String inputFile;

    /**
     * 其他参数集合
     */
    private Map otherMap;

}
