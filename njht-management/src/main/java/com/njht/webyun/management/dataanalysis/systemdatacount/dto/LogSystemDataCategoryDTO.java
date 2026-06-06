package com.njht.webyun.management.dataanalysis.systemdatacount.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 数据统计DTO
 *
 * @author zhouhouliang
 * @date 2021/7/14 13:35
 */
@Data
@ToString
@EqualsAndHashCode
public class LogSystemDataCategoryDTO implements Serializable {

    private Long id;

    private Boolean assertDownload;

    private String dataType;

    private String dataName;

    private Integer dataTypeSort;

    private Integer dataNameSort;

    private String matchRegexJson;

    private Map<String, String> matchRegexMap;

    private String path;

    private List<String> pathList;

}
