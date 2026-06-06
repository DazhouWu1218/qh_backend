package com.njht.webyun.publish.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("htht_cluster_schedule_product_info")
public class ProductInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
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
	private String cycle;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private String issue;
	/**
	 * 
	 */
	private String mark;
	/**
	 * 
	 */
	private String mapUrl;
	/**
	 * 
	 */
	private String productPath;
	/**
	 * 
	 */
	private String gdbPath;
	/**
	 * 
	 */
	private String productId;
	/**
	 * 
	 */
	private String regionId;
	/**
	 * 
	 */
	private Integer isRelease;
	/**
	 * 
	 */
	private String bz;
	/**
	 * 
	 */
	private String mosaicFile;
	/**
	 * 
	 */
	private String inputFileName;
	/**
	 * 
	 */
	private String modelIdentify;
	/**
	 * 卫星
	 */
	private String satellite;
	/**
	 * 传感器
	 */
	private String sensor;
	/**
	 * 分辨率
	 */
	private String resolution;


	@TableField(exist = false)
	private String treeId;
}
