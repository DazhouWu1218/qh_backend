package com.njht.webyun.management.business.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: HthtProductTree
 * @Description: 产品目录树
 * @author chensi
 * @date 2018年5月10日
 * 
 */
public class ProductTree implements Serializable
{

	/** serialVersionUID*/  
	private static final long serialVersionUID = 1L;

	/** id*/
	private String id;

	/** 父id*/
	private String parentId;

	/** 图标路径*/
	private String iconPath;

	/** */
	private String name;

	private String mark;

	/** */
	private String mapurl;

	/** */
	private String sortno;

	/** 虚拟id （仅用于发布平台展示）*/
	private String virtualId;

	/** 虚拟父id（仅用于发布平台展示）*/
	private String virtualParentId;

	/** 子产品目录*/
	private List<ProductTree> subTree;

	public ProductTree()
	{
		super();
	}

	public List<ProductTree> getSubTree() {
		return subTree;
	}

	public void setSubTree(List<ProductTree> subTree)
	{
		this.subTree = subTree;
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the parentId
	 */
	public String getParentId()
	{
		return parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	/**
	 * @return the iconPath
	 */
	public String getIconPath()
	{
		return iconPath;
	}

	/**
	 * @param iconPath
	 *            the iconPath to set
	 */
	public void setIconPath(String iconPath)
	{
		this.iconPath = iconPath;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getMapurl() {
		return mapurl;
	}

	public void setMapurl(String mapurl) {
		this.mapurl = mapurl;
	}

	public String getSortno() {
		return sortno;
	}

	public void setSortno(String sortno) {
		this.sortno = sortno;
	}
	
	public String getVirtualId() {
		return virtualId;
	}

	public void setVirtualId(String virtualId) {
		this.virtualId = virtualId;
	}

	public String getVirtualParentId() {
		return virtualParentId;
	}

	public void setVirtualParentId(String virtualParentId) {
		this.virtualParentId = virtualParentId;
	}

	@Override
	public String toString()
	{
		return "HthtProductTree [id=" + id + ", parentId=" + parentId + ", iconPath=" + iconPath + ", name=" + name
				+ ", subTree=" + subTree + "]";
	}

}
