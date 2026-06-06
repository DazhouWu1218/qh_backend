package com.njht.webyun.management.business.entity;

import java.util.Date;

/**
 * 实体
 * @author dgj
 * @since 1.0
 *
 */
public class HthtDataCategory {

	private static final long serialVersionUID = -1076166566400461140L;

	/** id*/
	private String id;

	/** 记录创建时间 */
	private Date createTime;

	/** 记录更新时间 */
	private Date updateTime;

	/** 乐观锁 */
	private Integer version;

	/** 父级id */
	private String parentId;

	/** 分类名 */
	private String name;

	/** 排序字段 */
	private Integer sortKey;

	/** 树节点属于哪颗树 */
	private String treeKey;

	private String mark;

	public HthtDataCategory() {
	}

	public HthtDataCategory(String id, Integer version,String parentId, String name, Integer sortKey, String treeKey) {
		this.id = id;
		this.version = version;
		this.parentId = parentId;
		this.name = name;
		this.sortKey = sortKey;
		this.treeKey = treeKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSortKey() {
		return sortKey;
	}

	public void setSortKey(Integer sortKey) {
		this.sortKey = sortKey;
	}

	public String getTreeKey() {
		return treeKey;
	}

	public void setTreeKey(String treeKey) {
		this.treeKey = treeKey;
	}

	public String getMark() {
		return mark;
	}

	public HthtDataCategory setMark(String mark) {
		this.mark = mark;
		return this;
	}
}