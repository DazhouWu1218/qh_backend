package com.njht.webyun.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * InnoDB free: 13312 kB
 * @author tianjm
 * @date 2019-11-11
 */
@ApiModel(value="Org",description="机构信息")
public class SysOrg {
	
	@ApiModelProperty(value="机构ID")
    private Integer orgId;

	@ApiModelProperty(value="机构编码")
    @NotNull(message = "orgCode")
    private String orgCode;

	@ApiModelProperty(value="机构名称")
    @NotNull(message = "orgName")
    private String orgName;

	@ApiModelProperty(value="父ID")
    @NotNull(message = "parentId")
    private Integer parentId;

    @ApiModelProperty(value="父继承ID")
	private String parentInheritedId;;

	@ApiModelProperty(value="继承机构名称")
    private String inheritedName;

	@ApiModelProperty(value="继承机构ID")
    private String inheritedId;

	@ApiModelProperty(value="序号")
    private Integer sortNum;

	@ApiModelProperty(value="层数")
    private Integer levelNum;

	@ApiModelProperty(value="联系电话")
    private String tel;

	@ApiModelProperty(value="地址")
    private String address;

	@ApiModelProperty(value="是否删除（0-未删除   1-已删除）")
    private Integer deleted;

	@ApiModelProperty(value="创建人")
    private Integer createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty(value="创建日期")
    private Date createdDate;

	@ApiModelProperty(value="最后更新人")
    private Integer lastUpdatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty(value="最后更新日期")
    private Date lastUpdatedDate;

	@ApiModelProperty(value="备注")
    private String remark;


    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentInheritedId() {
        return parentInheritedId;
    }

    public void setParentInheritedId(String parentInheritedId) {
        this.parentInheritedId = parentInheritedId;
    }

    public String getInheritedName() {
        return inheritedName;
    }

    public void setInheritedName(String inheritedName) {
        this.inheritedName = inheritedName;
    }

    public String getInheritedId() {
        return inheritedId;
    }

    public void setInheritedId(String inheritedId) {
        this.inheritedId = inheritedId;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}