package com.njht.webyun.system.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author David
 * @Time 2019年11月7日 下午5:12:37
 */
@ApiModel(value="BasePageModel",description="分页model")
public class BasePageModel {

	@ApiModelProperty(value="分页 页数",required=true)
	private Integer page;
	@ApiModelProperty(value="分页 每页数量",required=true)
	private Integer rows;

	public Integer getPage() {
		return (page != null && page > 0 ? page : 1);
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return (rows != null && rows > 0 ? rows : 10);
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}
}
