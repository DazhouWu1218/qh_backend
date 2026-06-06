package com.htht.executor.task.service.base.impl;

import com.htht.executor.download.entity.DownloadFileInfoEntity;
import com.htht.executor.task.constant.JobDownloadConstant;
import com.htht.executor.task.service.BaseDownJobService;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author 代国军
 * @description: modis 数据下载
 * @date 2022/5/18 10:36
 */
@Service(JobDownloadConstant.SOURCE_MODIS)
public class ModisDownloadJobService extends BaseDownJobService {

    @Override
    public void setRealFileName(DownloadFileInfoEntity downLoadInfo, String forFilePath){
        downLoadInfo.setRealFileName(new File(forFilePath).getName().split("\\?")[0]);
    }
}
