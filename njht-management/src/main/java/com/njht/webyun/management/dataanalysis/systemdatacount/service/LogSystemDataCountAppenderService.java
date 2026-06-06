package com.njht.webyun.management.dataanalysis.systemdatacount.service;

public interface LogSystemDataCountAppenderService {

    /**
     * 累加系统各类数据的 接入,下载信息
     *
     * @param dataName
     * @param dataSize
     */
    Long incrementSystemDataDownload(String dataName, Long dataSize);

}
