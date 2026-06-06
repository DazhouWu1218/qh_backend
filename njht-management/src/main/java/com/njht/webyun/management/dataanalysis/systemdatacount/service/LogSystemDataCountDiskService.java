package com.njht.webyun.management.dataanalysis.systemdatacount.service;

/**
 * 统计信息
 * 统计系统中接入的各类数据的容量
 *
 * @author zhouhouliang
 * @date 2021/7/14 13:32
 */
public interface LogSystemDataCountDiskService {

    /**
     * 计算数据接收
     */
    void calculateDataReceive();


    /**
     * 计算数据的下载
     */
    void calculateDataDownload();

}
