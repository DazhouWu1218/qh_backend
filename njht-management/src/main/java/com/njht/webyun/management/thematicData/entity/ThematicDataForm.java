package com.njht.webyun.management.thematicData.entity;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/4/26 16:51
 * @Description: 专题数据后台返回参数
 */
public class ThematicDataForm {

    private String id;
    private String thematicId;
    private String fileName;
    private String time;
    private String dataSource;
    private String dataType;
    private String cycle;
    private String cycleName;
    private Integer imgNum;
    private String fileUrl;
    private String mark;
    private List<String> timeList;
    private List<String> issueList;

    public ThematicDataForm() {
    }


    public String getDataType() {
        return dataType;
    }

    public ThematicDataForm(String id, String thematicId, String fileName,
                            String time, String dataSource, String dataType, String cycle, String fileUrl, String mark,
                            List<String> timeList,List<String> issueList) {
        this.id = id;
        this.thematicId = thematicId;
        this.fileName = fileName;
        this.time = time;
        this.dataSource = dataSource;
        this.dataType = dataType;
        this.cycle = cycle;
        this.fileUrl = fileUrl;
        this.mark = mark;
        this.timeList = timeList;
        this.issueList = issueList;
    }

    public String getMark() {
        return mark;
    }

    public ThematicDataForm setMark(String mark) {
        this.mark = mark;
        return this;
    }

    public ThematicDataForm setDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }


    public String getId() {
        return id;
    }

    public ThematicDataForm setId(String id) {
        this.id = id;
        return this;
    }

    public String getThematicId() {
        return thematicId;
    }

    public ThematicDataForm setThematicId(String thematicId) {
        this.thematicId = thematicId;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public ThematicDataForm setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getTime() {
        return time;
    }

    public ThematicDataForm setTime(String time) {
        this.time = time;
        return this;
    }

    public String getDataSource() {
        return dataSource;
    }

    public ThematicDataForm setDataSource(String dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public String getCycle() {
        return cycle;
    }

    public ThematicDataForm setCycle(String cycle) {
        this.cycle = cycle;
        return this;
    }

    public Integer getImgNum() {
        return imgNum;
    }

    public ThematicDataForm setImgNum(Integer imgNum) {
        this.imgNum = imgNum;
        return this;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public ThematicDataForm setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    public String getCycleName() {
        return cycleName;
    }

    public ThematicDataForm setCycleName(String cycleName) {
        this.cycleName = cycleName;
        return this;
    }

    public List<String> getTimeList() {
        return timeList;
    }

    public ThematicDataForm setTimeList(List<String> timeList) {
        this.timeList = timeList;
        return this;
    }

    public List<String> getIssueList() {
        return issueList;
    }

    public ThematicDataForm setIssueList(List<String> issueList) {
        this.issueList = issueList;
        return this;
    }
}
