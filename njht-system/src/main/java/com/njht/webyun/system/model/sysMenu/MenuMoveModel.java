package com.njht.webyun.system.model.sysMenu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/*
 * @author David
 * @Time 2019年11月18日 上午9:55:19
 */
@ApiModel(value="MenuMoveModel",description="菜单树拖拽信息")
public class MenuMoveModel implements Serializable{
	private static final long serialVersionUID = 1L;

	@NotNull(message = "ID不能为空")
	@ApiModelProperty(value="菜单ID")
    private Integer menuId;

	@NotNull(message = "父ID不能为空")
	@ApiModelProperty(value="父ID")
    private Integer parentId;

	@NotNull(message = "父菜单层级不能为空")
	@ApiModelProperty(value="父菜单层级")
    private Integer parentLevelNum;

	@NotNull(message = "menu不能为空")
	@ApiModelProperty(value="拖动后新的同级菜单model")
	private List<SysMenu> menu;

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
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

	public List<SysMenu> getMenu() {
		return menu;
	}

	public void setMenu(List<SysMenu> menu) {
		this.menu = menu;
	}
}
