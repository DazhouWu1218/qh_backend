package com.njht.webyun.system.model.sysRoute;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class RouteResp {

    @ApiModelProperty(value = "路由ID")
    private Integer routeId;

    @ApiModelProperty(value = "路由名称")
    private String routeName;

    @ApiModelProperty(value = "路由路径")
    private String routeUrl;

    @ApiModelProperty(value = "创建者")
    private String createdName;

    @ApiModelProperty(value = "创建时间")
    private Date createdDate;

    @ApiModelProperty(value = "最后修改者")
    private String lastUpdatedName;

    @ApiModelProperty(value = "最后修改时间")
    private Date lastUpdatedDate;

    public RouteResp() {
    }

    public RouteResp(Integer routeId, String routeName, String routeUrl, String createdName, Date createdDate, String lastUpdatedName, Date lastUpdatedDate) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.routeUrl = routeUrl;
        this.createdName = createdName;
        this.createdDate = createdDate;
        this.lastUpdatedName = lastUpdatedName;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
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

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastUpdatedName() {
        return lastUpdatedName;
    }

    public void setLastUpdatedName(String lastUpdatedName) {
        this.lastUpdatedName = lastUpdatedName;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}
