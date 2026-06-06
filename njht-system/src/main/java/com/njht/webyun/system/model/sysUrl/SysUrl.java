package com.njht.webyun.system.model.sysUrl;

import com.njht.webyun.system.model.sysRole.SysRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel(value="SysUrl",description="系统URL")
public class SysUrl {
    @ApiModelProperty(value="url表ID",required=true)
    private Integer urlId;

    @ApiModelProperty(value="角色")
    private List<SysRole> roles;

    @ApiModelProperty(value="URL名称",required=true)
    private String urlName;

    @ApiModelProperty(value="URL名称",required=true)
    private String urlNameEn;

    @ApiModelProperty(value="URL地址",required=true)
    private String urlPattern;

    @ApiModelProperty(value="请求方式",required=true)
    private String httpMethod;

    @ApiModelProperty(value="动作代码（备用）",required=true)
    private String actionCode;

    @ApiModelProperty(value="参数代码（备用）",required=true)
    private String argsCode;

    @ApiModelProperty(value="记录是否修改（0-未改  1-已改）",required=true)
    private Integer loggedDataChanged;

    @ApiModelProperty(value="创建人",required=true)
    private Integer createdBy;

    @ApiModelProperty(value="创建日期",required=true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDate;

    @ApiModelProperty(value="修改人",required=true)
    private Integer lastUpdatedBy;

    @ApiModelProperty(value="修改人姓名",required=true)
    private String updatedByName;

    @ApiModelProperty(value="修改日期",required=true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdatedDate;

    //  用于UpdateInterceptor
    private Integer pk_urlId;

    public Integer getPk_urlId() {
        return pk_urlId;
    }

    public void setPk_urlId(Integer pk_urlId) {
        this.pk_urlId = pk_urlId;
    }

    public Integer getUrlId() {
        return urlId;
    }

    public void setUrlId(Integer urlId) {
        this.urlId = urlId;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName == null ? null : urlName.trim();
    }

    public String getUrlNameEn() {
        return urlNameEn;
    }

    public void setUrlNameEn(String urlNameEn) {
        this.urlNameEn = urlNameEn;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern == null ? null : urlPattern.trim();
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod == null ? null : httpMethod.trim();
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode == null ? null : actionCode.trim();
    }

    public String getArgsCode() {
        return argsCode;
    }

    public void setArgsCode(String argsCode) {
        this.argsCode = argsCode == null ? null : argsCode.trim();
    }

    public Integer getLoggedDataChanged() {
        return loggedDataChanged;
    }

    public void setLoggedDataChanged(Integer loggedDataChanged) {
        this.loggedDataChanged = loggedDataChanged;
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

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }
}