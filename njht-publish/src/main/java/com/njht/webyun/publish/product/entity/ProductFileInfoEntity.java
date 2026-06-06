package com.njht.webyun.publish.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
@Data
@TableName("htht_cluster_schedule_product_file_info")
public class ProductFileInfoEntity implements Serializable {
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
	private String filePath;
	/**
	 * 
	 */
	private String fileType;
	/**
	 * 产品表里的id
	 */
	private String productId;
	/**
	 * 产品类型，专题图、报告、其他文件等
	 */
	private String productType;
	/**
	 * 是否删除 0有效，zt=1删除
	 */
	private String isDel;
	/**
	 * 
	 */
	private String relativePath;
	/**
	 * 
	 */
	private String cycle;
	/**
	 * 
	 */
	private String issue;
	/**
	 * 
	 */
	private String menuId;
	/**
	 * 
	 */
	private String region;
	/**
	 * 
	 */
	private String zt;
	/**
	 * 
	 */
	private String fileName;
	/**
	 * 
	 */
	private Long fileSize;
	/**
	 * 
	 */
	private String productInfoId;

}
