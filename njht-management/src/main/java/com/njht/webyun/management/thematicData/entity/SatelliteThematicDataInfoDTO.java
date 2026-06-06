package com.njht.webyun.management.thematicData.entity;

import lombok.Data;

import java.util.List;

/**
 * 专题数据
 * @author zhushizhen
 */
@Data
public class SatelliteThematicDataInfoDTO {
    /**
     * 主键id
     */
    private String id;

    /**
     * 树结构id
     */
    private String treeId;

    /**
     * 文件名称
     */
    private String fileName;

    private String issue;

    private String mark;

    private String cycle;
    /**
     * 文件类型
     */
    private String fileType;

    private Long fileSize;

    private String region;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 空间分辨率
     */
    private String spaceResolution;
    /**
     * 时间分辨率
     */
    private String timeResolution;

    private PngCoordinateInfo pngCoordinateInfo;
    /**
     * 对应的预处理照片
     */
    private List<SatelliteThematicFileDataInfoDTO> listFiles;


}
