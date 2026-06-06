package com.htht.job.core.biz.model;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * 传参信息
 * 请不要对该对象进行修改，需要添加参数 通过输入参数map 进行传参
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-10 10:15:42
 */
@Data
public class TaskParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 动态输入参数
	 */
	private LinkedHashMap<String,Object> dynamicMap;
	/**
	 * 算法参数
	 */
	private LinkedHashMap<String,Object> fixedMap;
	/**
	 * 任务id
	 */
	private Integer jobId;
	/**
	 * 调度参数
	 */
	private String modelParameters;

	/**
	 * 产品id
	 */
	private String productId;

	/**
	 * 是否重做，默认false
	 */
	private boolean retryTask = false;

}
