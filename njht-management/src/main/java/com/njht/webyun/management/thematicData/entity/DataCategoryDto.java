package com.njht.webyun.management.thematicData.entity;


import com.njht.webyun.management.business.entity.HthtDataCategory;

/**
 * @Author: 代国军
 * @CreateDate: 2021/4/19 14:36
 * @Description: 专题数据集分层
 */
public class DataCategoryDto extends HthtDataCategory {

    private String treeId;

    /** 周期 */
    private String cycle;

    /** 数据类型 */
    private String dataType;

    /** 数据源 */
    private String dataSource;

    /** 数据类型 */
    private String fileType;

    private String unit;
    private String max;
    private String min;
    private String isAno;
    private String mark;
    public DataCategoryDto() {
    }

    public String getTreeId() {
        return treeId;
    }

    public DataCategoryDto setTreeId(String treeId) {
        this.treeId = treeId;
        return this;
    }

    public String getCycle() {
        return cycle;
    }

    public DataCategoryDto setCycle(String cycle) {
        this.cycle = cycle;
        return this;
    }

    public String getDataType() {
        return dataType;
    }

    public DataCategoryDto setDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public String getDataSource() {
        return dataSource;
    }

    public DataCategoryDto setDataSource(String dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public String getFileType() {
        return fileType;
    }

    public DataCategoryDto setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public DataCategoryDto setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public String getMax() {
        return max;
    }

    public DataCategoryDto setMax(String max) {
        this.max = max;
        return this;
    }

    public String getMin() {
        return min;
    }

    public DataCategoryDto setMin(String min) {
        this.min = min;
        return this;
    }

    public String getIsAno() {
        return isAno;
    }

    public DataCategoryDto setIsAno(String isAno) {
        this.isAno = isAno;
        return this;
    }

    @Override
    public String getMark() {
        return mark;
    }

    @Override
    public DataCategoryDto setMark(String mark) {
        this.mark = mark;
        return this;
    }
}
