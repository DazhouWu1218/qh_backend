package com.htht.job.admin.ftp.vo;

import com.njht.webyun.valid.SaveGroup;
import com.njht.webyun.valid.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * 调度平台-FTP配置信息表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-06-27 17:05:24
 */
@Data
@ApiModel
public class FtpVo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@Null(message = "新增id必须为空",groups = {SaveGroup.class})
	@NotEmpty(message = "修改id不能为空",groups = {UpdateGroup.class})
	@ApiModelProperty(value = "id")
	private String id;

	/**
	 * ip地址
	 */
	@NotEmpty(message = "IP地址不能为空",groups = {SaveGroup.class})
	@ApiModelProperty(value = "ip地址")
	private String ipAddr;

	/**
	 * 名称
	 */
	@NotEmpty(message = "名称不能为空",groups = {SaveGroup.class})
	@ApiModelProperty(value = "名称")
	private String name;
	/**
	 * 端口号
	 */
	@ApiModelProperty(value = "端口号",example = "21")
	@NotNull(message = "端口号不能为空",groups = {SaveGroup.class})
	private Integer port;
	/**
	 * ftp密码
	 */
	@ApiModelProperty(value = "密码")
	@NotEmpty(message = "密码不能为空",groups = {SaveGroup.class})
	private String pwd;
	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	@NotEmpty(message = "用户名不能为空",groups = {SaveGroup.class})
	private String userName;

}
