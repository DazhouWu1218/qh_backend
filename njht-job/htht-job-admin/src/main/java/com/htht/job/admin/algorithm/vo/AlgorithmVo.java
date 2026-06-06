package com.htht.job.admin.algorithm.vo;

import com.njht.webyun.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

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
	 * 主键
	 */
	@ApiModelProperty("主键id")
	private String id;
	/**
	 * 父级id
	 */
	@ApiModelProperty("父级id")
	private String parentId;
	/**
	 * 名称
	 */
	@ApiModelProperty("名称")
	private String name;

	/**
	 * 类型 0目录 1数据
	 */
	@ApiModelProperty("类型 0目录 1数据")
	private String type;

}
