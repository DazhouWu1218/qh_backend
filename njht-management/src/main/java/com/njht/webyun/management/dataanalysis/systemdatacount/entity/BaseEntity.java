package com.njht.webyun.management.dataanalysis.systemdatacount.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * BaseEntity
 *
 * @author zhouhouliang
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BaseEntity {

    @TableId("id")
    private Long id;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(value = "version", fill = FieldFill.INSERT_UPDATE)
    private Long version;

}
