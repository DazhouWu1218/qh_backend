package com.njht.webyun.system.model.sysUser;


import com.njht.webyun.system.model.base.BasePageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author David
 * @Time 2019年11月7日 下午4:24:57
 */
@ApiModel(value="UserQuery",description="分页查询用户")
public class UserQuery extends BasePageModel {
	
	@ApiModelProperty(value="用户名",required=true)
    private String username;
	@ApiModelProperty(value="机构ID",required=true)
    private int orgId;
	@ApiModelProperty(value="是否包含子机构",required=true)
    private boolean containSub;
	@ApiModelProperty(value="排序字段",required=false)
	private String sort;
	@ApiModelProperty(value="排序方式",required=false)
	private String order;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public boolean isContainSub() {
		return containSub;
	}
	public void setContainSub(boolean containSub) {
		this.containSub = containSub;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
}
