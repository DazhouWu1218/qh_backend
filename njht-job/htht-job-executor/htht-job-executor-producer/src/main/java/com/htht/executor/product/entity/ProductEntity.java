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
@TableName("htht_cluster_schedule_product")
public class ProductEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

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
	 * 是否需要审核
	 */
	private Integer isRelease;
	/**
	 * 地图路径
	 */
	private String mapUrl;
	/**
	 * 产品标识
	 */
	private String mark;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 
	 */
	private String sortNo;
	/**
	 * 产品标识
	 */
	private String productPath;
	/**
	 * 树结构id
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
