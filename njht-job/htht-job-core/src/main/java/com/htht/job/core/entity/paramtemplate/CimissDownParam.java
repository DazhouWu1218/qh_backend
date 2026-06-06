package com.htht.job.core.entity.paramtemplate;

import lombok.Data;

@Data
public class CimissDownParam {

    private String dataCode;//资料名称
    private String interfaceId;//接口名称
    private String time;//时间点
    private String timeRange;//时间段
    private String elements;//要素字段
    private String staIds;//站号，多个用,逗号分隔
    private String specialElements;//判断是否已存在的要素字段
    private String filename;//自定义的文件名及文件格式
    private String filePath;//自定义的下载文件保存路径
    private String adminCodes;//区域行政编号
    private String eleValueRanges;
    private String toTxt;

    private String dataFormat; // 数据格式
    private String otherParam;


}
