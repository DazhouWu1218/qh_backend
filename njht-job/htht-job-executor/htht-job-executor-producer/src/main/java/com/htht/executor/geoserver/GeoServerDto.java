package com.htht.executor.geoserver;

import lombok.Data;

import java.io.File;

@Data
public class GeoServerDto {

    /**
     * 工作空间
     */
    private String workspace;

    /**
     * 数据存储名称
     */
    private String storeName;

    /**
     * 数据源名称
     */
    private String coverageName;

    /**
     * geoTiff文件
     */
    private File geoTiff;

    /**
     * shp 压缩文件
     */
    private File zipFile;

    /**
     * shp 图层名称
     */
    private String layerName;

    /**
     * 坐标系称
     */
    private String projection;

    /**
     * 表名
     */
    private String tableName;


}
