package com.njht.webyun.business.index.vo;

import com.njht.webyun.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 算法管理Vo
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-30 13:06:10
 */
@ApiModel("算法目录请求AlgorithmVo")
@Data
public class AlgorithmVo extends PageEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 父级id
	 */
	@ApiModelProperty(value = "id,全部传 0,其余传首页算法列表id",example = "0")
	@NotNull(message = "父id不能为空")
	private String parentId;
	/**
	 * 名称
	 */
	@ApiModelProperty("名称")
	private String name;


	private String type = "1";

}
