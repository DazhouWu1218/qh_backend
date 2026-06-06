package com.htht.executor.download.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("htht_cluster_schedule_cimiss_conn_info")
public class CimissDownInfoEntity implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 版本
     */
    private Integer version;

    /**
     * ip 地址
     */
    private String ipAddr;

    /**
     * 名称
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * 端口
     */
    private String port;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 账户名称
     */
    private String userName;

}
