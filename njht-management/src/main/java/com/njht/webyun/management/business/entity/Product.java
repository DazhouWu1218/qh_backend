package com.njht.webyun.management.business.entity;

import java.util.Date;

/**
 * 实体
 * @author dgj
 * @since 1.0
 *
 */
public class Product  {
	
	private static final long serialVersionUID = 7981322391231607218L;

	/** 主键id */
	private String id;

	/** 创建时间 */
	private Date createTime;

	/** 修改时间 */
	private Date updateTime;

	/** 版本号 */
	private Integer version;

	/**  */
	private String bz;

	/**  */
	private String featureName;

	/**  */
	private String gdbPath;

	/**  */
	private String iconPath;

	/**  */
	private Integer isRelease;

	/** 地图路径 */
	private String mapUrl;

	/** 标记 */
	private String mark;

	/** 名称 */
	private String name;

	/**  */
	private String sortNo;

	/** 产品路径 */
	private String productPath;

	/** 级别id */
	private String treeId;

	/**  */
	private String issue;

	/**  */
	private String featurename;

	/**  */
	private String gdbpath;

	/**  */
	private String mapurl;

	/**  */
	private String menu;

	/**  */
	private String menuId;

	/**  */
	private String storagepath;

	/**  */
	private String userid;

	/**  */
	private String triggerParam;

	public Product() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
	
	public String getFeatureName() {
		return this.featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	
	public String getGdbPath() {
		return this.gdbPath;
	}

	public void setGdbPath(String gdbPath) {
		this.gdbPath = gdbPath;
	}
	
	public String getIconPath() {
		return this.iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	
	public Integer getIsRelease() {
		return this.isRelease;
	}

	public void setIsRelease(Integer isRelease) {
		this.isRelease = isRelease;
	}
	
	public String getMapUrl() {
		return this.mapUrl;
	}

	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl;
	}
	
	public String getMark() {
		return this.mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getSortNo() {
		return this.sortNo;
	}

	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}
	
	public String getProductPath() {
		return this.productPath;
	}

	public void setProductPath(String productPath) {
		this.productPath = productPath;
	}
	
	public String getTreeId() {
		return this.treeId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}
	
	public String getIssue() {
		return this.issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}
	
	public String getFeaturename() {
		return this.featurename;
	}

	public void setFeaturename(String featurename) {
		this.featurename = featurename;
	}
	
	public String getGdbpath() {
		return this.gdbpath;
	}

	public void setGdbpath(String gdbpath) {
		this.gdbpath = gdbpath;
	}
	
	public String getMapurl() {
		return this.mapurl;
	}

	public void setMapurl(String mapurl) {
		this.mapurl = mapurl;
	}
	
	public String getMenu() {
		return this.menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}
	
	public String getMenuId() {
		return this.menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	
	public String getStoragepath() {
		return this.storagepath;
	}

	public void setStoragepath(String storagepath) {
		this.storagepath = storagepath;
	}
	
	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getTriggerParam() {
		return this.triggerParam;
	}

	public void setTriggerParam(String triggerParam) {
		this.triggerParam = triggerParam;
	}
	
}