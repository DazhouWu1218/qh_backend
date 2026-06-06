package com.njht.webyun.management.business.entity;

import lombok.Data;

import java.util.Date;

/**
 * 实体
 * @author dgj
 * @since 1.0
 *
 */
@Data
public class ProductCategory {
	
	private static final long serialVersionUID = 7151817022917799482L;

	private String id;

	/**  */
	private Date createTime;

	/**  */
	private Date updateTime;

	/**  */
	private Integer version;

	/**  */
	private String iconPath;

	/**  */
	private String menu;

	/**  */
	private String menuId;

	/**  */
	private String parentId;

	/**  */
	private String text;

	/**  */
	private String treeKey;

	private Integer sortKey;

	public ProductCategory() {
	}

	
}