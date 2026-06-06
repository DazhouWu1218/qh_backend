package com.htht.job.admin.algorithm.vo;

import com.njht.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 算法管理Vo
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-30 13:06:10
 */
@ApiModel("算法目录请求AlgorithmVo")
@Data
public class AlgorithmReqVo extends BaseEntity {
	private static final long serialVersionUID = 1L;

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
	 * 排序字段
	 */
	@ApiModelProperty("排序字段")
	private Integer sortKey;
	/**
	 * 算法路径
	 */
	@ApiModelProperty("算法路径")
	private String algorithmPath;
	/**
	 * 算法类型
	 */
	@ApiModelProperty("算法类型")
	private String algorithmType;
	/**
	 * 算法版本
	 */
	@ApiModelProperty("算法版本")
	private String algorithmVersion;
	/**
	 * 插件id
	 */
	@ApiModelProperty("插件id")
	private String handlerId;

	@ApiModelProperty("插件id 集合")
	private List<String> handlerIdArr;

	@ApiModelProperty("插件名称")
	private String handlerName;
	/**
	 * 执行器id
	 */
	@ApiModelProperty("执行器id")
	private Integer groupId;
	/**
	 * 节点ip 以及对应端口，多个节点拿逗号分隔开，绑定算法执行节点
	 */
	@ApiModelProperty("节点ip 以及对应端口，多个节点拿逗号分隔开")
	@NotEmpty(message = "算法必须绑定执行节点")
	private String registryValue;
	/**
	 * 类型 0目录 1数据
	 */
	@ApiModelProperty("类型 0目录 1数据")
	private String type;

	@ApiModelProperty("模板id")
	private String templateId;

}
