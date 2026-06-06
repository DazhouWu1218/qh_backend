package com.njht.webyun.management.order.entity;

import lombok.Data;

import java.util.Date;

/**
 * 实体
 * @author dgj
 * @since 1.0
 *
 */
@Data
public class OrderInfo {
	
	private static final long serialVersionUID = -2322271988091479040L;

	private Integer id;

	/** 订单号 */
	private String orderId;

	/** 订单名称 */
	private String orderName;

	/** 数据用途 */
	private String orderUse;

	/** 创建时间 */
	private Date createTime;

	/**  */
	private Date updateTime;

	/**  */
	private String userId;

	/** 订购人名称 */
	private String realName;

	/** 文件数量 */
	private Integer fileNum;

	/** 文件大小 */
	private String fileSize;
	private String username;

	/** 订单状态（6种状态）
		1: 待审核
		2：审核通过
		3：审核不通过
		4：数据准备中
		5：订单完成
		6:  订单失效 */
	private Short orderState;

	/** 是否审核:
		0 : false,
		1 : true */
	private Byte state;

	/** 订单未通过原因 */
	private String orderReason;

	/** 下载地址 */
	private String downloadPath;

	
}