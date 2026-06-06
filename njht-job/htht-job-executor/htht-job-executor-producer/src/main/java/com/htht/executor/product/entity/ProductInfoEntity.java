package com.htht.executor.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-10 18:06:35
 */
@Data
@TableName("htht_cluster_schedule_product_info")
public class ProductInfoEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

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

}
