package com.htht.job.admin.dispatch.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by piesat on 16/9/30.
 */
@ApiModel(value = "执行器管理 新增,编辑传参")
@TableName("xxl_job_group")
public class XxlJobGroup {

    @ApiModelProperty(value = "修改id")
    @TableId(type = IdType.AUTO)
    private int id;
    @ApiModelProperty(value = "执行器编码")
    private String appname;
    @ApiModelProperty(value = "名称")
    private String title;
    @ApiModelProperty(value = "执行器地址类型：0=自动注册、1=手动录入")
    private int addressType;
    @ApiModelProperty(value = "执行器地址列表，多地址逗号分隔(手动录入)")
    private String addressList;
    private Date updateTime;

    // registry list
    private List<String> registryList;  // 执行器地址列表(系统注册)
    public List<String> getRegistryList() {
        if (addressList!=null && addressList.trim().length()>0) {
            registryList = new ArrayList<String>(Arrays.asList(addressList.split(",")));
        }
        return registryList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    public String getAddressList() {
        return addressList;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }

}
