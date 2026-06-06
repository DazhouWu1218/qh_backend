package com.njht.webyun.management.dataanalysis.systemdatacount.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统各种数据接入,下载的统计日志
 *
 * @author zhouhouliang
 */
@Data
@TableName("htht_uus_log_sys_data_category")
public class LogSystemDataCategoryEntity extends BaseEntity {

    private Boolean assertDownload;

    private String dataType;

    private String dataName;

    private Integer dataTypeSort;

    private Integer dataNameSort;

    private String matchRegexJson;

    private String path;

}
