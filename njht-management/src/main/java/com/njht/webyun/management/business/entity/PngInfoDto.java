package com.njht.webyun.management.business.entity;

import java.util.List;

/**
 * @author Administrator
 */
public class PngInfoDto {

    private String filePath;
    private String name;
    private List<PngInfo> PngInfoList;

    public PngInfoDto() {
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PngInfo> getPngInfoList() {
        return PngInfoList;
    }

    public void setPngInfoList(List<PngInfo> pngInfoList) {
        PngInfoList = pngInfoList;
    }
}
