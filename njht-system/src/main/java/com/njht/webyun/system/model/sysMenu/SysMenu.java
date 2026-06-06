package com.njht.webyun.system.model.sysMenu;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(value="SysMenu",description="系统菜单")
public class SysMenu implements Serializable {
    @ApiModelProperty(value="菜单ID")
    private Integer menuId;
    @ApiModelProperty(value="菜单名称")
    private String menuName;
    @ApiModelProperty(value="路由ID")
    private Integer routeId;
    @ApiModelProperty(value="路由URL")
    private String routeUrl;
    @ApiModelProperty(value="图标")
    private String icon;
    @ApiModelProperty(value="父ID")
    private Integer parentId;
    @ApiModelProperty(value="业务")
    private String business;
    @ApiModelProperty(value="序号")
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
    @ApiModelProperty(value="子菜单")
    private List<SysMenu> children;

    //  用户sql查询tree结构的（子节点查询用的）
    private Integer userId;

    //  用于UpdateInterceptor
    private Integer pk_menuId;

    public Integer getPk_menuId() {
        return pk_menuId;
    }

    public void setPk_menuId(Integer pk_menuId) {
        this.pk_menuId = pk_menuId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
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

    public List<SysMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getRouteUrl() {
        return routeUrl;
    }

    public void setRouteUrl(String routeUrl) {
        this.routeUrl = routeUrl;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }
}