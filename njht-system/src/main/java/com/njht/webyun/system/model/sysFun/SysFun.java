package com.njht.webyun.system.model.sysFun;

import com.njht.webyun.system.model.sysRole.SysRole;
import com.njht.webyun.system.model.sysUrl.SysUrl;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@ApiModel(value="SysFun",description="系统功能")
public class SysFun {
    @ApiModelProperty(value="功能ID")
    private Integer funId;
    @ApiModelProperty(value="功能名称")
    private String funName;
    @ApiModelProperty(value="父ID")
    private Integer parentId;
    @ApiModelProperty(value="序号")
    @NotNull(message = "sortNum 不能为空")
    private Integer sortNum;
    @ApiModelProperty(value="层级")
    private Integer levelNum;
    @ApiModelProperty(value="是否删除 0-否 1-是")
    private Integer deleted;
    @ApiModelProperty(value="创建人")
    private Integer createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value="创建日期")
    private Date createdDate;
    @ApiModelProperty(value="最后更新人")
    private Integer lastUpdatedBy;
    @ApiModelProperty(value="最后更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdatedDate;
    @ApiModelProperty(value="角色")
    private List<SysRole> roles;
    @ApiModelProperty(value="URL")
    private List<SysUrl> urls;
    @ApiModelProperty(value="子菜单")
    private List<SysFun> children;

    //用于功能绑定URL的传参 20210308
    private List<Integer> urlId;

    public List<Integer> getUrlId() {
        return urlId;
    }

    public void setUrlId(List<Integer> urlId) {
        this.urlId = urlId;
    }

    //  用于UpdateInterceptor
    private Integer pk_funId;

    public Integer getPk_funId() {
        return pk_funId;
    }

    public void setPk_funId(Integer pk_funId) {
        this.pk_funId = pk_funId;
    }

    public List<SysFun> getChildren() {
        return children;
    }

    public void setChildren(List<SysFun> children) {
        this.children = children;
    }

    public Integer getFunId() {
        return funId;
    }

    public void setFunId(Integer funId) {
        this.funId = funId;
    }

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public Integer getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(Integer levelNum) {
        this.levelNum = levelNum;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public List<SysUrl> getUrls() {
        return urls;
    }

    public void setUrls(List<SysUrl> urls) {
        this.urls = urls;
    }
}