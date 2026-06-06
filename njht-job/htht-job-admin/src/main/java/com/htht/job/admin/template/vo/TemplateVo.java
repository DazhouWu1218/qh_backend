package com.htht.job.admin.template.vo;

import com.njht.webyun.valid.SaveGroup;
import com.njht.webyun.valid.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;

/**
 * 模板参数
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-06-29 09:14:41
 */
@Data
@ApiModel(value = "模板目录传参")
public class TemplateVo  {

	/**
	 * id
	 */
	@Null(message = "新增id必须为空",groups = {SaveGroup.class})
	@NotEmpty(message = "修改id不能为空",groups = {UpdateGroup.class})
	@ApiModelProperty(value = "id")
	private String id;

	/**
	 * 父id
	 */
	@ApiModelProperty(value = "父id,根节点父id默认为0")
	@NotEmpty(message = "父id 不能为空,根节点为0",groups = SaveGroup.class)
	private String parentId;

	/**
	 * 模板名称
	 */
	@ApiModelProperty(value = "模板名称")
	@NotEmpty(message = "模板名称不能为空",groups = {SaveGroup.class,UpdateGroup.class})
	private String name;


}
