package com.njht.webyun.publish.region.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;

import lombok.Data;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 17:11:52
 */
@Data
@TableName("htht_region_info")
public class RegionInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 区域id
	 */
	@TableId
	private String regionid;
	/**
	 * 父id
	 */
	private String tMRegionid;
	/**
	 * 区域级别
	 */
	private Integer regionlevel;
	/**
	 * 名称
	 */
	private String fullname;
	/**
	 * 
	 */
	private String districtname;
	/**
	 * 
	 */
	private BigDecimal enable;
	/**
	 * 
	 */
	private Double bottom;
	/**
	 * 
	 */
	private Double lat;
	/**
	 * 
	 */
	private Double left;
	/**
	 * 
	 */
	private Double lon;
	/**
	 * 
	 */
	private Double right;
	/**
	 * 
	 */
	private Double top;
	/**
	 * 地形图
	 */
	@TableField(exist = false)
	private String theGemo;


	private Integer sortKey;

}
