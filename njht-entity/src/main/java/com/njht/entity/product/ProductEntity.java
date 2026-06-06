package com.njht.entity.product;

import com.baomidou.mybatisplus.annotation.TableName;
import com.njht.entity.base.BaseEntity;
import lombok.Data;

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
	 * 是否需要审核
	 */
	private Integer isRelease;

	/**
	 * 产品标识
	 */
	private String mark;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 产品标识
	 */
	private String productPath;
	/**
	 * 树结构id
	 */
	private String treeId;



}
