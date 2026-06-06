package com.njht.webyun.system.model.sysRoute;

import com.njht.webyun.system.model.base.BasePageModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class RouteRes extends BasePageModel {

    @ApiModelProperty(value = "路由名称")
    private String routeName;

    @ApiModelProperty(value = "路由路径")
    private String routeUrl;

    @ApiModelProperty(value = "路由ID")
    private Integer routeId;

    @ApiModelProperty(value="创建人",required=true)
    private Integer createdBy;

    @ApiModelProperty(value="创建日期",required=true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDate;

    @ApiModelProperty(value="修改人",required=true)
    private Integer lastUpdatedBy;

    @ApiModelProperty(value="修改日期",required=true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdatedDate;

    public RouteRes() {
    }

    public RouteRes(String routeName, String routeUrl, Integer routeId, Integer createdBy, Date createdDate, Integer lastUpdatedBy, Date lastUpdatedDate) {
        this.routeName = routeName;
        this.routeUrl = routeUrl;
        this.routeId = routeId;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteUrl() {
        return routeUrl;
    }

    public void setRouteUrl(String routeUrl) {
        this.routeUrl = routeUrl;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}
