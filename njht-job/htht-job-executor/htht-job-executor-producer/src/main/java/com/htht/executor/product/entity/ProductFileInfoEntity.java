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
@TableName("htht_cluster_schedule_product_file_info")
public class ProductFileInfoEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

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
