package com.njht.webyun.product.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
@Data
public class DataCategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String id;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 
	 */
	private Integer version;
	/**
	 * 
	 */
	private String iconPath;
	/**
	 * 
	 */
	private String menu;
	/**
	 * 
	 */
	private String menuId;
	/**
	 * 
	 */
	private String parentId;
	/**
	 * 
	 */
	private String text;
	/**
	 * 
	 */
	private String treeKey;
	/**
	 * 
	 */
	private Integer sortKey;

	private String imgUrl;
	private Integer isHot;
	private String hotImgUrl;
	private Integer isIndex;


}
