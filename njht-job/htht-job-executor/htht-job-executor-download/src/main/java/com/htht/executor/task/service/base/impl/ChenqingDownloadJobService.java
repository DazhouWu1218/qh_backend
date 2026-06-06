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
import lombok.extern.slf4j.Slf4j;
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
@Service(JobDownloadConstant.CHENQING)
@Slf4j
public class ChenqingDownloadJobService extends BaseDownJobService {

    @Autowired
    private FtpService ftpService;

    @Override
    public List<String> downLoadSourceData(Set<String> pathList, DownParam downParam, Date beginTime, Date endTime, List<String> existFileList) throws IOException {
        List<String> fileList = new ArrayList<String>();
        for (String filePath : pathList) {
            FtpEntity ftpDTO = ftpService.getById(downParam.getForFtp());
            ApacheFtpUtil ftpUtil = new ApacheFtpUtil(ftpDTO);
            if (ftpUtil.connectServer()) {
                // 辰青数据 获取所有文件并返回
                List<String> list = new ArrayList<>();
                log.info("文件路径：{}",filePath);
                ftpUtil.getDataFileAndDirectoryList(filePath, downParam.getDownFileNamePattern(),list);
                ftpUtil.closeServer();
                for (String originalFilePath : list) {
                    File file = new File(originalFilePath);
                    String redisKey = "FtpDownLoad:ChengQing:" + file.getPath();
                    if (redisService.exists(redisKey)) {
                        continue;
                    }
                    // 文件名中的时间
                    long time = DataTimeHelper.getDataTimeFromFileNameByPattern(file.getName(), downParam.getDataTimePattern());
                    Date fileDate = new Date(time);

                    // 判断该时间是否在开始和结束时间之间
                    if (fileDate.after(endTime) || fileDate.before(beginTime)) {
                        continue;
                    }
                    // 重命名
                    String downloadFileName = getDownloadFileName(downParam, file.getName(), fileDate);
                    // 目标路径 (复制 目标目录部分 替换输入目录部分，其余部分文件层级不变)
                    String toPath = DateUtil.getPathByDate(downParam.getToPath(), fileDate);
                    toPath = file.getParentFile().getPath().replace("\\","/").replace(filePath,toPath);
                    log.info("目标目录：{}",toPath);
                    // 判断是否已经下载了 （路径中的 \\ / 用# 代替了）
                    String dbPath = toPath.replace("\\", "#").replace("/", "#");
                    if (existFileList.contains(dbPath + "#" + downloadFileName)) {
                        continue;
                    }
                    // 目标路径 用文件的时间替换目标文件的通配符
                    String repToPath = toPath.replace("\\", "/");
                    if (!repToPath.endsWith("/")) {
                        repToPath += "/";
                    }
                    // 包括："原路径＋原名称＋","＋新路径＋新名称＋","+文件大小"+","+文件的时间 + "," + redisKey
                    fileList.add(file.getPath() + "," + repToPath + downloadFileName + "," + file.length()
                            + "," + fileDate.getTime() + "," + redisKey);
                    redisService.set(redisKey, redisKey);
                }
            }
        }
        return fileList;
    }

    @Override
    public Boolean down(TriggerParam triggerParam, DownloadFileInfoEntity downLoadInfo, DownParam downParam, String forFilePath){
        String toFilePath = downLoadInfo.getFilePath() + File.separator + downLoadInfo.getFileName();
        Long fileSize = downLoadInfo.getFileSize();
        return super.downLoadFromFtp(downParam, forFilePath, toFilePath, fileSize);
    }

}
