package com.htht.executor.download.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.executor.cimiss.bean.DownloadInfo;
import com.htht.executor.download.entity.DownloadFileInfoEntity;
import com.htht.job.core.entity.paramtemplate.CimissDownParam;

import java.util.Date;
import java.util.List;

/**
 * 数据下载表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-17 16:41:22
 */
public interface DownloadFileInfoService extends IService<DownloadFileInfoEntity> {

    /**
     * 根据开始结束时间，获取该时间段内的数据信息
     * @param beginTime
     * @param endTime
     * @return
     */
    List<String> findFilesByTime(Date beginTime, Date endTime);

    /**
     * 通过文件名删除文件
     *
     *
     * @param downloadFileName
     */
    void delByFileName(String downloadFileName);

    /**
     * 过滤已经从天擎上下载的文件信息
     * @param fileParamList
     * @return
     */
    List<DownloadInfo> filterCimissFileList(List<DownloadInfo> fileParamList, CimissDownParam cimissParam);

    /**
     * 数据入库
     * @param downLoadList
     */
    void saveListInfo(List<DownloadFileInfoEntity> downLoadList);
}

