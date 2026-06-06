package com.njht.webyun.management.dataanalysis.systemvisit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.njht.webyun.management.dataanalysis.systemdatacount.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

@TableName("htht_uus_log_sys_visit_count")
@Data
public class SystemVisitCountEntity extends BaseEntity {


    private String userId;

    private Long count;

    private Date lastVisitTime;

}
