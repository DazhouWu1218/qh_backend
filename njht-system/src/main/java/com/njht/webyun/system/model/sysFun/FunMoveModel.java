package com.njht.webyun.system.model.sysFun;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/*
 * @author David
 * @Time 2019年11月18日 上午9:55:19
 */
@ApiModel(value="FunMoveModel",description="功能树拖拽信息")
public class FunMoveModel implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="功能ID")
    private Integer id;
	
	@ApiModelProperty(value="父ID")
    private Integer parentId;
	
	@ApiModelProperty(value="父功能层级")
    private Integer parentLevelNum;

	@ApiModelProperty(value="拖动后新的同级功能model")
	private List<SysFun> sysfun;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public void setParentLevelNum(Integer parentLevelNum) {
		this.parentLevelNum = parentLevelNum;
	}

	public List<SysFun> getSysfun() {
		return sysfun;
	}

	public void setSysfun(List<SysFun> sysfun) {
		this.sysfun = sysfun;
	}
}
