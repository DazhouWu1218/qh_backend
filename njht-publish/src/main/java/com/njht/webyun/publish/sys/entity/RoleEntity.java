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
@TableName("sys_role")
public class RoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer roleId;
	/**
	 * 
	 */
	private String roleName;
	/**
	 * 
	 */
	private Integer orgId;
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
