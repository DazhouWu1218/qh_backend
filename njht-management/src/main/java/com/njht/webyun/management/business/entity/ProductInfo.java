package com.njht.webyun.management.business.entity;

import java.util.Set;

/**
 * 实体
 * @author dgj
 * @since 1.0
 *
 */
public class ProductInfo {
	
	private static final long serialVersionUID = -8427863533721548721L;

	/** 主键id */
	private String id;

	/** 创建时间 */
	private String createTime;

	/** 周期 */
	private String cycle;

	private String cycleName;

	/** 名字 */
	private String name;


	/** 地区id */
	private String regionId;

	/** 地区*/
	private String regionName;

	/** 卫星 */
	private String satellite;

	/** 期次 */
	private String issue;

	/** 标记 */
	private String mark;


	/**  */
	private String mosaicFile;

	/** 分辨率*/
	private String resolution;

	/** 传感器*/
	private String sensor;

	/** */
	private Set<String> types;

	/** 区分发不发服务*/
	private Integer geoServerType;

	public ProductInfo() {
	}

	public Integer getGeoServerType() {
		return geoServerType;
	}

	public ProductInfo setGeoServerType(Integer geoServerType) {
		this.geoServerType = geoServerType;
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getSatellite() {
		return satellite;
	}

	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getMosaicFile() {
		return mosaicFile;
	}

	public void setMosaicFile(String mosaicFile) {
		this.mosaicFile = mosaicFile;
	}

	public Set<String> getTypes() {
		return types;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getSensor() {
		return sensor;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
	}

	public void setTypes(Set<String> types) {
		this.types = types;
	}

}