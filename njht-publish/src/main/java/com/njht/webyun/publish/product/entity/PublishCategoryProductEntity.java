package com.njht.webyun.publish.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-08-18 16:51:47
 */
@Data
@TableName("publish_category_product")
public class PublishCategoryProductEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private String id;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 版本号
	 */
	private Integer version;
	/**
	 * 备注
	 */
	private String bz;
	/**
	 * 类型（监测，预测）
	 */
	private String type;
	/**
	 * 名称 （春小麦,等）
	 */
	private String name;
	/**
	 * 目录id
	 */
	private String treeId;
	/**
	 * 产品id
	 */
	private String productId;
	/**
	 * 数据源
	 */
	private String satellite;
	/**
	 * 周期（多个周期用逗号分隔）
	 */
	private String cycle;

}
