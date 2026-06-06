package com.htht.executor.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @date 2021-11-22 11:54:17
 */
@Data
@TableName("sys_dic")
public class DicEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 表ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer dicId;
	/**
	 * 字典名称
	 */
	private String dicName;
	/**
	 * 字典值
	 */
	private String dicType;
	/**
	 * 字典键
	 */
	private String dicKey;
	/**
	 * 字典类型
	 */
	private String dicValue;
	/**
	 * 象征
	 */
	private String dicSymbol;
	/**
	 * 创建时间
	 */
	private Date createdDate;
	/**
	 * 创建人ID
	 */
	private Integer createdBy;
	/**
	 * 修改时间
	 */
	private Date lastUpdatedDate;
	/**
	 * 更改人ID
	 */
	private Integer lastUpdatedBy;

}
