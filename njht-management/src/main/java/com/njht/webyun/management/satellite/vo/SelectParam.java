package com.njht.webyun.management.satellite.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lmd
 */
public class SelectParam {
    private List<Map> sateSensorId;
    private String sort;
    private Double resolution;
    private Double cloud;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Integer pageNum=1;
    private Integer pageSize=5;
    private List<Integer> dataType;
    private Map geo;
    private String regionId;


    public List<Map> getSateSensorId() {
        return sateSensorId;
    }

    public void setSateSensorId(List<Map> sateSensorId) {
        this.sateSensorId = sateSensorId;
    }

    public Map getGeo() {
        return geo;
    }

    public void setGeo(Map geo) {
        this.geo = geo;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Double getResolution() {
        return resolution;
    }

    public void setResolution(Double resolution) {
        this.resolution = resolution;
    }

    public Double getCloud() {
        return cloud;
    }

    public void setCloud(Double cloud) {
        this.cloud = cloud;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<Integer> getDataType() {
        return dataType;
    }

    public void setDataType(List<Integer> dataType) {
        this.dataType = dataType;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
