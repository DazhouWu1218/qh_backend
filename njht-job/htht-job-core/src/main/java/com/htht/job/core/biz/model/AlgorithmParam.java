package com.htht.job.core.biz.model;
import lombok.Data;

import java.io.Serializable;

/**
 * 算法管理表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-30 13:06:10
 */
@Data
public class AlgorithmParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	private String id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 算法路径
	 */
	private String algorithmPath;
	/**
	 * 算法类型
	 */
	private String algorithmType;
	/**
	 * 算法版本
	 */
	private String algorithmVersion;

	/**
	 * 节点ip 以及对应端口，多个节点拿逗号分隔开，绑定算法执行节点
	 */
	private String registryValue;


}
