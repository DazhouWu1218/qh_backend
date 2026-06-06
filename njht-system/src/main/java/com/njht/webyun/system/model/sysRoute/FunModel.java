package com.njht.webyun.system.model.sysRoute;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class FunModel {

    @ApiModelProperty(value = "功能ID")
    private Integer funId;

    @ApiModelProperty(value = "功能编码")
    private String funCode;

    @ApiModelProperty(value = "功能名称")
    private String funName;

    @ApiModelProperty(value = "功能类型")
    private Integer funType;

    @ApiModelProperty(value = "关联的URLID")
    private Integer urlId;

    @ApiModelProperty(value = "关联的URL路径")
    private Integer routeId;

    @ApiModelProperty(value = "关联的路由ID")
    private String urlPattern;

    @ApiModelProperty(value = "创建人")
    private Integer createdBy;

    @ApiModelProperty(value = "创建时间")
    private Date createdDate;

    @ApiModelProperty(value = "最后修改人")
    private Integer lastUpdatedBy;

    @ApiModelProperty(value = "最后修改时间")
    private Date lastUpdatedDate;


    public FunModel() {
    }

    public FunModel(Integer funId, String funCode, String funName, Integer funType, Integer urlId, Integer routeId, Integer createdBy, Date createdDate, Integer lastUpdatedBy, Date lastUpdatedDate) {
        this.funId = funId;
        this.funCode = funCode;
        this.funName = funName;
        this.funType = funType;
        this.urlId = urlId;
        this.routeId = routeId;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Integer getFunId() {
        return funId;
    }

    public void setFunId(Integer funId) {
        this.funId = funId;
    }

    public String getFunCode() {
        return funCode;
    }

    public void setFunCode(String funCode) {
        this.funCode = funCode;
    }

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public Integer getFunType() {
        return funType;
    }

    public void setFunType(Integer funType) {
        this.funType = funType;
    }

    public Integer getUrlId() {
        return urlId;
    }

    public void setUrlId(Integer urlId) {
        this.urlId = urlId;
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

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }
}
