package com.njht.webyun.management.basis.entity;

import java.util.Date;

/**
 * @author ：daiguojun
 * @date ：Created in 2021/3/4 17:21
 * @description：基础数据实体类
 */
public class BasisDataInfo {

    private static final long serialVersionUID = -5648204918089292853L;

    /** id*/
    private String  id;

    /** 名称 */
    private String name;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;

    /** 树id */
    private String treeId;

    /** 乐观锁 */
    private Integer version;

    /** 数据对应表名 */
    private String tableName;

    /** 类型 区分 有没有时间 0,1 默认为1，有时间条件的设置为0*/
    private Integer type;

    private String searchField;

    private String filePath;
    private String fileSize;

    public BasisDataInfo() {
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTreeId() {
        return this.treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }
}
