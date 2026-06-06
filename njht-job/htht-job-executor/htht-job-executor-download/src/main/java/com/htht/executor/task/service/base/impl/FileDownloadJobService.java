package com.htht.executor.task.service.base.impl;

import com.htht.executor.download.entity.DownloadFileInfoEntity;
import com.htht.executor.task.constant.JobDownloadConstant;
import com.htht.executor.task.service.BaseDownJobService;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.date.DataTimeHelper;
import com.htht.job.core.entity.paramtemplate.DownParam;
import com.htht.job.core.util.DateUtil;
import com.htht.job.core.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service(JobDownloadConstant.SOURCE_FILE)
@Slf4j
public class FileDownloadJobService extends BaseDownJobService {

    @Override
    public List<String> downLoadSourceData(Set<String> pathList, DownParam downParam, Date beginTime, Date endTime, List<String> existFileList) throws IOException {
        List<String> fileList = new ArrayList<>();
        for (String filePath : pathList) {
            // 得到符合文件名正则的文件
            List<File> files = FileUtil.iteratorFileAndDirectory(new File(filePath), downParam.getDownFileNamePattern());
            if (files == null) {
                continue;
            }
            for (File file : files) {
                String redisKey = "FileDownLoad:" + file.getName();
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
                toPath = file.getParentFile().getPath().replace(filePath,toPath);
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
                fileList.add(file.getAbsolutePath() + "," + repToPath + downloadFileName + "," + file.length()
                        + "," + fileDate.getTime() + "," + redisKey);
                redisService.set(redisKey, redisKey);
            }
        }
        return fileList;
    }


    /**
     * 下载源 是 文件，分文件下载到文件，和文件下载到FTP 两种情况
     * @param triggerParam 参数
     * @param downLoadInfo 下载信息
     * @param downParam 模板参数
     * @return
     */
    @Override
    public Boolean down(TriggerParam triggerParam, DownloadFileInfoEntity downLoadInfo, DownParam downParam,String forFilePath){
        Boolean flag = false;
        String toFilePath = downLoadInfo.getFilePath() + File.separator + downLoadInfo.getFileName();
        Long fileSize = downLoadInfo.getFileSize();
        // 从 文件 复制 到文件
        if (JobDownloadConstant.SOURCE_FILE.equals(downParam.getToSouceType())) {
            File localFile = new File(toFilePath);
            if (localFile.exists() && localFile.length() != fileSize) {
                localFile.delete();
                FileUtil.copy(forFilePath, toFilePath + ".tmp");
                File file2 = new File(toFilePath + ".tmp");
                while (file2.exists() && file2.renameTo(file2)) {
                    file2.renameTo(localFile);
                }
            } else {
                FileUtil.copy(forFilePath, toFilePath + ".tmp");
                File file2 = new File(toFilePath + ".tmp");
                while(file2.exists() && file2.renameTo(file2)) {
                    file2.renameTo(localFile);
                }
            }
            flag = true;
        } else if (JobDownloadConstant.SOURCE_FTP.equals(downParam.getToSouceType())) {
            // 从 文件上传到 FTP
            flag = uploadToFtp(downParam, forFilePath, toFilePath);
        } else if (JobDownloadConstant.SOURCE_SFTP.equals(downParam.getToSouceType())) {
            // 从文件上传到 SFTP
            flag = super.uploadToSftp(downParam, toFilePath, super.getTempFile(triggerParam, toFilePath));
        }
        return flag;
    }

}
