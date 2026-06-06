package com.htht.executor.task.service;

import com.htht.executor.download.entity.DownloadFileInfoEntity;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.entity.paramtemplate.DownParam;
import com.htht.job.core.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 代国军
 * @description: 下载任务服务层
 * @date 2022/5/18 9:10
 */
public interface DownJobServiceInterface {

    /**
     * 数据下载任务 handler
     * @param downParam 参数模板
     */
    void execute(DownParam downParam);


    /**
     * 数据下载 任务分片
     * @param taskParam
     * @return
     */
    ReturnT<List<String>> executeShard(TaskParam taskParam);

    /**
     * 获取要下载的数据集
     * @param pathList
     * @param downParam
     * @param beginTime
     * @param endTime
     * @param existFileList
     * @return
     * @throws IOException
     */
    default List<String> downLoadSourceData(Set<String> pathList, DownParam downParam, Date beginTime, Date endTime, List<String> existFileList) throws IOException {
        return new ArrayList<>();
    }

    /**
     * 设置文件名
     * @param downLoadInfo
     * @param forFilePath
     */
    default void setRealFileName(DownloadFileInfoEntity downLoadInfo, String forFilePath){
        downLoadInfo.setRealFileName(new File(forFilePath).getName());
    }

    /**
     * 下载方法
     * @param triggerParam
     * @param downLoadInfo
     * @param downParam
     * @param forFilePath 源文件路径
     * @return
     */
    default Boolean down(TriggerParam triggerParam, DownloadFileInfoEntity downLoadInfo,DownParam downParam,String forFilePath){
        return false;
    }

    /**
     * 获取下载文件名
     * @param downParam
     * @param fileName
     * @param fileDate
     * @return
     */
    default String getDownloadFileName(DownParam downParam,String fileName, Date fileDate) {
        String downloadFileName = fileName;
        if ((StringUtils.isEmpty(downParam.getDownloadFileName()) || !"-1".equals(downParam.getDownloadFileName()))
                && !downParam.getDownloadFileName().contains("(")) {
            downloadFileName = DateUtil.getPathByDate(downParam.getDownloadFileName(), fileDate);
        }
        if (!StringUtils.isEmpty(downParam.getDownloadFileName())
                && downParam.getDownloadFileName().contains("(")) {
            String renameFilePattern = downParam.getDownloadFileName();
            Integer begin = Integer.parseInt(renameFilePattern
                    .substring(renameFilePattern.indexOf("(") + 1, renameFilePattern.indexOf(")")));
            downloadFileName = downloadFileName.substring(begin);
        }

        if (!downloadFileName.contains(".")) {
            downloadFileName += fileName.substring(fileName.lastIndexOf("."));
        }
        return downloadFileName;
    }

}
