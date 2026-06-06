package com.njht.webyun.publish.collect.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-12 14:53:36
 */
@Data
@TableName("publish_collection_info")
public class CollectionInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.AUTO)
	private String id;

	/**
	 * 收藏文件信息
	 */
	private String productFileInfoId;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createdDate;
	/**
	 * 创建者
	 */
	private Integer createdBy;
	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date lastUpdatedDate;
	/**
	 * 最后修改人
	 */
	private Integer lastUpdatedBy;
	/**
	 * 逻辑删除（0 1）
	 */
	@TableLogic(value = "0",delval = "1")
	private Integer deleted;

}
