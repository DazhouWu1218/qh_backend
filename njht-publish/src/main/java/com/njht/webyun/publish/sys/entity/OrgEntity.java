package com.njht.webyun.publish.sys.entity;

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
 * @date 2021-11-30 20:05:56
 */
@Data
@TableName("sys_org")
public class OrgEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer orgId;
	/**
	 * 
	 */
	private String orgCode;
	/**
	 * 名称简写
	 */
	private String orgNameLabel;
	/**
	 * 名称
	 */
	private String orgName;
	/**
	 * 
	 */
	private String parentId;
	/**
	 * 
	 */
	private String inheritedId;
	/**
	 * 
	 */
	private String inheritedName;
	/**
	 * 
	 */
	private Integer sortNum;
	/**
	 * 
	 */
	private Integer levelNum;
	/**
	 * 
	 */
	private String tel;
	/**
	 * 
	 */
	private String address;
	/**
	 * 
	 */
	private String remark;
	/**
	 * 
	 */
	private Integer deleted;
	/**
	 * 
	 */
	private Integer createdBy;
	/**
	 * 
	 */
	private Date createdDate;
	/**
	 * 
	 */
	private Integer lastUpdatedBy;
	/**
	 * 
	 */
	private Date lastUpdatedDate;

}
