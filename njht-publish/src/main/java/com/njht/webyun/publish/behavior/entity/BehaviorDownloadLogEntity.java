package com.njht.webyun.publish.behavior.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-12-08 09:09:54
 */
@Data
@TableName("publish_behavior_download_log")
public class BehaviorDownloadLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 下载id 关联发布平台日志表
	 */
	private Integer behaviorId;
	/**
	 * 产品名称
	 */
	private String produceName;
	/**
	 * 卫星
	 */
	private String satellite;
	/**
	 * 周期
	 */
	private String cycle;
	/**
	 * 分辨率
	 */
	private String resolution;
	/**
	 * 区域名称
	 */
	private String regionId;
	/**
	 * 是否删除（0-未删除，1-已删除）
	 */
	private Integer deleted;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createdDate;
	/**
	 * 创建人
	 */
	private Integer createdBy;
	/**
	 * 最后修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date lastUpdatedDate;
	/**
	 * 最后修改人
	 */
	private Integer lastUpdatedBy;
	/**
	 * 文件类型（逗号分隔开）
	 */
	private String fileType;
	/**
	 * 文件个数
	 */
	private Integer fileNum;
	/**
	 * 文件大小
	 */
	private Long fileSize;
	/**
	 * 树结构id
	 */
	private String menuId;
	/**
	 * 期次信息
	 */
	private String issue;

	@TableField(exist = false)
	private String regionName;

}
