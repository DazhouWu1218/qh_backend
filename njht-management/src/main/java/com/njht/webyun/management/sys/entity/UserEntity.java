package com.njht.webyun.management.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-26 09:38:22
 */
@Data
@TableName("sys_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	@TableId
	private Integer userId;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 用户密码
	 */
	private String pwd;
	/**
	 * 机构ID
	 */
	private Integer orgId;
	/**
	 * 是否锁定（0-未锁定，1-已锁定）
	 */
	private Integer locked;
	/**
	 * 用户密码输错次数
	 */
	private Integer errorTime;
	/**
	 * 年龄
	 */
	private Integer age;
	/**
	 * 用户生日
	 */
	private Date birthday;
	/**
	 * 固定电话
	 */
	private String phone;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 电子邮件
	 */
	private String email;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 用户头像图片文件
	 */
	private String userPicData;
	/**
	 * 用户头像文件后缀
	 */
	private String userPicSuffix;
	/**
	 * 关于用户签名
	 */
	private String about;
	/**
	 * 最后登录时间
	 */
	private Date lastLoginDate;
	/**
	 * 最后登录ip
	 */
	private String lastLoginIp;
	/**
	 * 是否删除（0-未删除，1-已删除）
	 */
	private Integer deleted;
	/**
	 * 创建时间
	 */
	private Date createdDate;
	/**
	 * 创建人
	 */
	private Integer createdBy;
	/**
	 * 最后修改时间
	 */
	private Date lastUpdatedDate;
	/**
	 * 最后修改人
	 */
	private Integer lastUpdatedBy;

}
