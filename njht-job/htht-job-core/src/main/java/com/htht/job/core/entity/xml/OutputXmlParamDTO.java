package com.htht.job.core.entity.xml;

import lombok.Data;
import lombok.ToString;

/**
 * 输出xml参数
 */
@Data
@ToString
public class OutputXmlParamDTO {
    /**
     * 缓存路径
     */
    private String temp;

    /**
     *  输出xml文件路径
     */
    private String outXMLPath;

    /**
     * 输出日志文件路径
     */
    private String outLogPath;

    /**
     * 输出文件夹
     */
    private String outFolder;
}
