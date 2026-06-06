package com.njht.entity.category;

import com.baomidou.mybatisplus.annotation.TableName;
import com.njht.entity.base.BaseEntity;
import lombok.Data;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
@Data
@TableName("htht_cluster_schedule_data_category")
public class DataCategoryEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String iconPath;
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
	private String parentId;
	/**
	 * 
	 */
	private String text;
	/**
	 * 
	 */
	private String treeKey;
	/**
	 * 
	 */
	private Integer sortKey;

	private String imgUrl;
	private Integer isHot;
	private String hotImgUrl;
	private Integer isIndex;


}
