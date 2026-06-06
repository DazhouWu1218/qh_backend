package com.njht.webyun.publish.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户角色表
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-30 20:05:56
 */
@Data
@TableName("sys_role_user")
public class RoleUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色户表ID
	 */
	@TableId
	private Integer roleId;
	/**
	 * 用户表ID
	 */
	private Integer userId;

}
