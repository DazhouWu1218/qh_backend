package com.njht.webyun.management.thematicData.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;


/**
 * @Author: 代国军
 * @CreateDate: 2021/4/19 16:06
 * @Description: 专题数据集前端参数
 */
public class ThematicDataParam {

    @Valid
    @NotNull
    @Size(min = 1,message = "最少选一个数据")
    private List<ThematicDataInfoParam> dataList;

    @NotNull(message = "行政区域不能为空")
    private String regionId;

    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Integer pageNum;

    private Integer pageSize;

    public ThematicDataParam() {
    }

    public List<ThematicDataInfoParam> getDataList() {
        return dataList;
    }

    public ThematicDataParam setDataList(List<ThematicDataInfoParam> dataList) {
        this.dataList = dataList;
        return this;
    }

    public String getRegionId() {
        return regionId;
    }

    public ThematicDataParam setRegionId(String regionId) {
        this.regionId = regionId;
        return this;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public ThematicDataParam setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
        return this;
    }

    public Date getEndTime() {
        return endTime;
    }

    public ThematicDataParam setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public ThematicDataParam setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public ThematicDataParam setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}
