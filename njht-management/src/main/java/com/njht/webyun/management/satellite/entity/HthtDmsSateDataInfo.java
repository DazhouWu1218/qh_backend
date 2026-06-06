package com.njht.webyun.management.satellite.entity;


import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * @author miaoyu
 * @date 2020-05-25 03:19:51
 */

public class HthtDmsSateDataInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private String id;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date updateTime;
    /**
     *
     */
    private String satelliteId;
    /**
     *
     */
    private String sensorId;
    /**
     *
     */
    private java.sql.Date date;
    /**
     *
     */
    private Time time;
    /**
     *
     */
    private String name;
    /**
     *
     */
    private Double resolution;
    /**
     *
     */
    private String type;
    /**
     *
     */
    private Integer bands;
    /**
     *
     */
    private Double cloud;
    /**
     *
     */
    private Double topleftlatitude;
    /**
     *
     */
    private Double topleftlongitude;
    /**
     *
     */
    private Double toprightlatitude;
    /**
     *
     */
    private Double toprightlongitude;
    /**
     *
     */
    private Double bottomleftlatitude;
    /**
     *
     */
    private Double bottomleftlongitude;
    /**
     *
     */
    private Double bottomrightlatitude;
    /**
     *
     */
    private Double bottomrightlongitude;
    /**
     *
     */
    private Double centerlongitude;
    /**
     *
     */
    private Double centerlatitude;
    /**
     *
     */
    private Double area;
    /**
     *
     */
    private String level;
    /**
     *
     */
    private Long fileSize;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private List<HthtDmsSateDataFileInfo> fileInfos;

    public List<HthtDmsSateDataFileInfo> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<HthtDmsSateDataFileInfo> fileInfos) {
        this.fileInfos = fileInfos;
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

    public String getSatelliteId() {
        return satelliteId;
    }

    public void setSatelliteId(String satelliteId) {
        this.satelliteId = satelliteId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Integer getBands() {
        return bands;
    }

    public void setBands(Integer bands) {
        this.bands = bands;
    }

    public Double getTopleftlatitude() {
        return topleftlatitude;
    }

    public void setTopleftlatitude(Double topleftlatitude) {
        this.topleftlatitude = topleftlatitude;
    }

    public Double getTopleftlongitude() {
        return topleftlongitude;
    }

    public void setTopleftlongitude(Double topleftlongitude) {
        this.topleftlongitude = topleftlongitude;
    }

    public Double getToprightlatitude() {
        return toprightlatitude;
    }

    public void setToprightlatitude(Double toprightlatitude) {
        this.toprightlatitude = toprightlatitude;
    }

    public Double getToprightlongitude() {
        return toprightlongitude;
    }

    public void setToprightlongitude(Double toprightlongitude) {
        this.toprightlongitude = toprightlongitude;
    }

    public Double getBottomleftlatitude() {
        return bottomleftlatitude;
    }

    public void setBottomleftlatitude(Double bottomleftlatitude) {
        this.bottomleftlatitude = bottomleftlatitude;
    }

    public Double getBottomleftlongitude() {
        return bottomleftlongitude;
    }

    public void setBottomleftlongitude(Double bottomleftlongitude) {
        this.bottomleftlongitude = bottomleftlongitude;
    }

    public Double getBottomrightlatitude() {
        return bottomrightlatitude;
    }

    public void setBottomrightlatitude(Double bottomrightlatitude) {
        this.bottomrightlatitude = bottomrightlatitude;
    }

    public Double getBottomrightlongitude() {
        return bottomrightlongitude;
    }

    public void setBottomrightlongitude(Double bottomrightlongitude) {
        this.bottomrightlongitude = bottomrightlongitude;
    }

    public Double getCenterlongitude() {
        return centerlongitude;
    }

    public void setCenterlongitude(Double centerlongitude) {
        this.centerlongitude = centerlongitude;
    }

    public Double getCenterlatitude() {
        return centerlatitude;
    }

    public void setCenterlatitude(Double centerlatitude) {
        this.centerlatitude = centerlatitude;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
