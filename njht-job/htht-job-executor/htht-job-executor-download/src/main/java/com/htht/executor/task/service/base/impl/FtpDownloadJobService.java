package com.htht.executor.task.service.base.impl;

import com.htht.executor.download.entity.DownloadFileInfoEntity;
import com.htht.executor.download.entity.FtpEntity;
import com.htht.executor.download.service.FtpService;
import com.htht.executor.task.constant.JobDownloadConstant;
import com.htht.executor.task.service.BaseDownJobService;
import com.htht.executor.task.util.ApacheFtpUtil;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.date.DataTimeHelper;
import com.htht.job.core.entity.paramtemplate.DownParam;
import com.htht.job.core.util.DateUtil;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 代国军
 * @description: ftp 数据下载
 * @date 2022/5/18 11:41
 */
@Service(JobDownloadConstant.SOURCE_FTP)
public class FtpDownloadJobService extends BaseDownJobService {

    @Autowired
    private FtpService ftpService;

    @Override
    public List<String> downLoadSourceData(Set<String> pathList, DownParam downParam, Date beginTime, Date endTime, List<String> existFileList) throws IOException {
        List<String> fileList = new ArrayList<String>();
        for (String filePath : pathList) {
            FtpEntity ftpDTO = ftpService.getById(downParam.getForFtp());
            ApacheFtpUtil ftpUtil = new ApacheFtpUtil(ftpDTO);
            if (ftpUtil.connectServer()) {
                FTPFile[] list = ftpUtil.getDataFileList(filePath, downParam.getDownFileNamePattern());
                ftpUtil.closeServer();
                for (FTPFile file : list) {
                    String originalFileName = file.getName();
                    String redisKey = "FtpDownLoad:" + originalFileName;
                    if (redisService.exists(redisKey)) {
                        continue;
                    }
                    //根据文件名时间判断是否在下载区间内
                    Date fileDate = new Date(DataTimeHelper.getDataTimeFromFileNameByPattern(file.getName(),
                            downParam.getDataTimePattern()));
                    boolean timeFilter = fileDate.after(endTime) || fileDate.before(beginTime);
                    if (timeFilter) {
                        continue;
                    }
                    // 重命名
                    String downloadFileName = super.getDownloadFileName(downParam, file.getName(), fileDate);
                    // 目标路径 用文件的时间替换目标文件的通配符
                    String toPath = DateUtil.getPathByDate(downParam.getToPath(), fileDate);
                    String repToPath = toPath.replace("\\", "/");
                    // 判断是否已经下载了 （路径中的 \\ / 用# 代替了）
                    String replace = toPath.replace("\\", "#").replace("/", "#");
                    if (existFileList.contains(replace + "#" + downloadFileName)) {
                        // 如果文件存在, 进行判断是否大小一致, 不一致删除数据库与文件, 重新下载
                        try {
                            File toPathFile = new File(repToPath, downloadFileName);
                            // 大小为byte的比较
                            long size = file.getSize() - toPathFile.length();
                            // 源文件与已有文件大小相差1byte
                            // 一致, 继续下一循环
                            if (Math.abs(size) < 1) {
                                continue;
                            }
                            // 不一致, 删除数据库记录, 删除目标文件
                            toPathFile.delete();
                            downloadFileInfoService.delByFileName(downloadFileName);
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    if (!repToPath.endsWith("/")) {
                        repToPath += "/";
                    }
                    repToPath = DateUtil.getPathByDate(repToPath, fileDate);
                    // 源路径 用文件的时间替换目标文件的通配符
                    filePath = filePath.replace("\\", "/");
                    if (!filePath.endsWith("/")) {
                        filePath += "/";
                    }
                    // 包括："原路径＋原名称＋","＋新路径＋新名称＋","+文件大小"+","+文件的时间
                    fileList.add(filePath + originalFileName + "," + repToPath + downloadFileName + "," + file.getSize() + "," + fileDate.getTime() + "," + redisKey);
                    redisService.set(redisKey, redisKey);
                }
            }
        }
        return fileList;
    }

    @Override
    public Boolean down(TriggerParam triggerParam, DownloadFileInfoEntity downLoadInfo, DownParam downParam, String forFilePath){
        Boolean flag = false;
        String toFilePath = downLoadInfo.getFilePath() + File.separator + downLoadInfo.getFileName();
        Long fileSize = downLoadInfo.getFileSize();
        // 从 FTP 下载到文件
        if (JobDownloadConstant.SOURCE_FILE.equals(downParam.getToSouceType())) {
            flag = super.downLoadFromFtp(downParam, forFilePath, toFilePath, fileSize);
        } else if (JobDownloadConstant.SOURCE_FTP.equals(downParam.getToSouceType())) {
            // 从 ftp上传到 FTP 先下载到本地，再从本地上传到FTP  (本地应有一个临时路径)
            File tempFile = super.getTempFile(triggerParam, toFilePath);
            // 下载到本地
            super.downLoadFromFtp(downParam,forFilePath,tempFile.getAbsolutePath(),downLoadInfo.getFileSize());
            // 从本地上传到ftp
            super.uploadToFtp(downParam,tempFile.getAbsolutePath(),toFilePath);
            // 删除临时文件
            tempFile.delete();
        } else if (JobDownloadConstant.SOURCE_SFTP.equals(downParam.getToSouceType())) {
            //sftp上传到ftp  (先下载到本地，再从本地上传)
            File tempFile = super.getTempFile(triggerParam, toFilePath);
            // 下载到本地
            super.downLoadFromFtp(downParam,forFilePath,tempFile.getAbsolutePath(),downLoadInfo.getFileSize());

            //sftp上传
            flag = uploadToSftp(downParam, toFilePath, tempFile);
            // 上传完成 删除临时文件
            tempFile.delete();
        }
        return flag;
    }

}
