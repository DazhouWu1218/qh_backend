package com.njht.webyun.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;

/**
 * @date 2019-11-01
 */
@ApiModel(value="SysRole",description="系统角色")
public class SysRole implements GrantedAuthority{
    private static final long serialVersionUID = 1L;
    //  用于UpdateInterceptor
    private Integer pk_roleId;
    public Integer getPk_roleId() {
        return pk_roleId;
    }
    public void setPk_roleId(Integer pk_roleId) {
        this.pk_roleId = pk_roleId;
    }
    @ApiModelProperty(value="角色表ID")
    private Integer roleId;
    @ApiModelProperty(value="角色名")
    private String roleName;
    @ApiModelProperty(value="机构id")
    private Integer orgId;
    @ApiModelProperty(value="备注")
    private String remark;
    @ApiModelProperty(value="是否删除")
    private Integer deleted;
    @ApiModelProperty(value="创建人ID")
    private Integer createdBy;
    @ApiModelProperty(value="创建人姓名")
    private String createdName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value="创建日期")
    private Date createdDate;
    @ApiModelProperty(value="更改人id")
    private Integer lastUpdatedBy;
    @ApiModelProperty(value="更改人姓名")
    private String updatedName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value="更改日期")
    private Date lastUpdatedDate;
    @ApiModelProperty(value="机构名称")
    private String orgName;
    @ApiModelProperty(value="机构层级名称")
    private String inheritedName;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public String getUpdatedName() {
        return updatedName;
    }

    public void setUpdatedName(String updatedName) {
        this.updatedName = updatedName;
    }

    public String getInheritedName() {
        return inheritedName;
    }

    public void setInheritedName(String inheritedName) {
        this.inheritedName = inheritedName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    //加上jsonIgnore注解，表示此属性不参与该bean的类型转换
    @JsonIgnore
    @Override
    public String getAuthority() {
        return roleName;
    }


}