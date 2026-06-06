package com.htht.job.core.entity.paramtemplate;

import lombok.Data;
import lombok.ToString;

/**
 * 动态参数实体类
 */
@Data
@ToString
public class DynamicParam {
    /**
     * 执行策略
     */
    private String businessService = "";

    /**
     * 搜索类别
     */
//    private String searchType = "";

    /**
     * pgsql表名
     */
    private String pgsqlTab = "";

    /**
     * 产品标识
     */
    private String productKey = "";

    /**
     * 分辨率
     */
//    private String resolution = "";

    /**
     * 单数据文件输入标识 1-是 0-否
     */
    private String is_single_produce = "1";

    /**
     * 输入文件是否为文件夹 1-是 0-否
     */
    private String is_directory = "0";

    /**
     * 文件名中时间格式
     */
    private String timeRegex = "yyyyMMdd_HHmm";

    /**
     * 是否保存统计数据到表中
     */
    private String saveStatistic = "0";

    /**
     * ImageMosaic 服务发布文件路径
     */
    private String publishPath = "";

    /**
     * 工作空间
     */
    private String workspace = "";

    /**
     * 数据存储名称
     */
    private String storeName = "";


    private String isToDb = "1";

    private String outFolder;


}
