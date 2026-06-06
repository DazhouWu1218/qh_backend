package com.njht.webyun.management.thematicData.entity;

/**
 * 实体
 * @author dgj
 * @since 1.0
 *
 */
public class HthtThematicDataInfo {
	
	private static final long serialVersionUID = 1895091901584333089L;

	private String	id;

	/** 产品树id */
	private String treeId;

	/** 周期 */
	private String cycle;

	/** 数据类型 */
	private String dataType;

	/** 数据源 */
	private String dataSource;

	/** 产品名称 */
	private String name;

	private String  mark;

	public HthtThematicDataInfo() {
	}

	public String getId() {
		return id;
	}

	public HthtThematicDataInfo setId(String id) {
		this.id = id;
		return this;
	}

	public String getTreeId() {
		return this.treeId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}
	
	public String getCycle() {
		return this.cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	
	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getDataSource() {
		return this.dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMark() {
		return mark;
	}

	public HthtThematicDataInfo setMark(String mark) {
		this.mark = mark;
		return this;
	}
}