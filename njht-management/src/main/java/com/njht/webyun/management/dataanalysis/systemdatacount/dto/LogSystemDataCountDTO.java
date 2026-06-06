package com.njht.webyun.management.dataanalysis.systemdatacount.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 各类数据 统计信息
 *
 * @author zhouhouliang
 */
@Data
public class LogSystemDataCountDTO implements Serializable {


    private Boolean assertDownload;

    private String dataType;

    private String dataName;

    private Integer dataTypeSort;

    private Integer dataNameSort;

    private Date date;

    private String dataSize;


}
