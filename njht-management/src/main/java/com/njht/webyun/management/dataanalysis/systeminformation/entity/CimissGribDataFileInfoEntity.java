package com.njht.webyun.management.dataanalysis.systeminformation.entity;

import java.io.Serializable;

/**
 * (CimissGribDataFileInfo)实体类
 *
 * @author makejava
 * @since 2021-06-29 09:57:01
 */
public class CimissGribDataFileInfoEntity implements Serializable {
    private static final long serialVersionUID = -58633691177323245L;

    private String id;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件类型 : tif jpg
     */
    private String fileType;
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 绝对路径
     */
    private String fileUrl;
    /**
     * 原始数据id
     */
    private String dataId;
    /**
     * 数据类型
     */
    private Integer dataType;

    private Double xStart;

    private Double yStart;

    private Double xEnd;

    private Double yEnd;
    /**
     * 海拔高度：层次
     */
    private String indexLayer;

    private String fileSize;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Double getXStart() {
        return xStart;
    }

    public void setXStart(Double xStart) {
        this.xStart = xStart;
    }

    public Double getYStart() {
        return yStart;
    }

    public void setYStart(Double yStart) {
        this.yStart = yStart;
    }

    public Double getXEnd() {
        return xEnd;
    }

    public void setXEnd(Double xEnd) {
        this.xEnd = xEnd;
    }

    public Double getYEnd() {
        return yEnd;
    }

    public void setYEnd(Double yEnd) {
        this.yEnd = yEnd;
    }

    public String getIndexLayer() {
        return indexLayer;
    }

    public void setIndexLayer(String indexLayer) {
        this.indexLayer = indexLayer;
    }

    public Double getxStart() {
        return xStart;
    }

    public void setxStart(Double xStart) {
        this.xStart = xStart;
    }

    public Double getyStart() {
        return yStart;
    }

    public void setyStart(Double yStart) {
        this.yStart = yStart;
    }

    public Double getxEnd() {
        return xEnd;
    }

    public void setxEnd(Double xEnd) {
        this.xEnd = xEnd;
    }

    public Double getyEnd() {
        return yEnd;
    }

    public void setyEnd(Double yEnd) {
        this.yEnd = yEnd;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
