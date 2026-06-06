package com.njht.webyun.management.region.entity;


import lombok.Data;

/**
 * 实体
 * @author dgj
 * @since 1.0
 *
 */
@Data
public class RegionInfo {
	
	private static final long serialVersionUID = 4899679550003037652L;

	/** 区域id */
	private String regionId;
	/** 父id */
	private String tMRegionid;

	/** 区域级别 */
	private String regionlevel;

	/**  区域名*/
	private String fullname;

	private String label;

	/**  */
	private String districtname;

	/**  */
	private Double enable;


	/** 地形图 */
	private String theGemo;

}