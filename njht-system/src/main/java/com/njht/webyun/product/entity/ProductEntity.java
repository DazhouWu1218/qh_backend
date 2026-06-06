package com.njht.webyun.product.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
@Data
public class ProductEntity implements Serializable {
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
	private String bz;
	/**
	 * 
	 */
	private String cycle;
	/**
	 * 
	 */
	private String featureName;
	/**
	 * 
	 */
	private String gdbPath;
	/**
	 * 
	 */
	private String iconPath;
	/**
	 * 
	 */
	private Integer isRelease;
	/**
	 * 
	 */
	private String mapUrl;
	/**
	 * 
	 */
	private String mark;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private String sortNo;
	/**
	 * 
	 */
	private String productPath;
	/**
	 * 
	 */
	private String treeId;
	/**
	 * 
	 */
	private String issue;
	/**
	 * 
	 */
	private String featurename;
	/**
	 * 
	 */
	private String gdbpath;
	/**
	 * 
	 */
	private String mapurl;
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
	private String storagepath;
	/**
	 * 
	 */
	private String userid;
	/**
	 * 
	 */
	private String triggerParam;

}
