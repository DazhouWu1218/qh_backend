package com.njht.webyun.system.model.sysOrg;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author David
 * @Time 2019年11月18日 上午9:55:19
 */
@ApiModel(value="OrgMoveModel",description="机构树拖拽信息")
public class OrgMoveModel implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="机构ID")
    private Integer orgId;
	
	@ApiModelProperty(value="父ID")
    private Integer parentId;
	
	@ApiModelProperty(value="父机构层级")
    private Integer parentLevelNum;
	
	@ApiModelProperty(value="继承机构名称")
    private String inheritedName;

	@ApiModelProperty(value="继承机构ID")
    private String inheritedId;
	
	@ApiModelProperty(value="机构model")
	private List<SysOrg> org;

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getParentLevelNum() {
		return parentLevelNum;
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

	public void setParentLevelNum(Integer parentLevelNum) {
		this.parentLevelNum = parentLevelNum;
	}

	public List<SysOrg> getOrg() {
		return org;
	}

	public void setOrg(List<SysOrg> org) {
		this.org = org;
	}
	
}
