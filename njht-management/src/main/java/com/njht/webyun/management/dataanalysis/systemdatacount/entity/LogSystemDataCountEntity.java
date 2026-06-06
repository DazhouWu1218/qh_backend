package com.njht.webyun.management.dataanalysis.systemdatacount.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * 系统各种数据接入,下载的统计日志
 *
 * @author zhouhouliang
 */
@Data
@TableName("htht_uus_log_sys_data_count")
@AllArgsConstructor
public class LogSystemDataCountEntity extends BaseEntity {

    private Long dataCategoryId;

    private Date date;

    private Long dataSize;

}
