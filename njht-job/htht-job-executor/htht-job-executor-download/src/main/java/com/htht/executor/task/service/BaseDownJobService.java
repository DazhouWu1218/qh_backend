package com.htht.executor.task.service;

import com.alibaba.fastjson.JSON;
import com.htht.executor.download.entity.DownloadFileInfoEntity;
import com.htht.executor.download.entity.FtpEntity;
import com.htht.executor.download.service.DownloadFileInfoService;
import com.htht.executor.download.service.FtpService;
import com.htht.executor.task.util.ApacheFtpUtil;
import com.htht.executor.task.util.SftpUtils;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.constant.DateConstant;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.entity.paramtemplate.DownParam;
import com.htht.job.core.exception.CommonException;
import com.htht.job.core.redis.RedisService;
import com.htht.job.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 代国军
 * @description: 下载
 * 调度过程中需要将 redis key set 到 XxlJobHelper 中, 回调会清理对应的redis信息，防止缓存脏数据
 * @date 2022/5/18 9:37
 */
@Slf4j
public abstract class BaseDownJobService implements DownJobServiceInterface {

    @Autowired
    public DownloadFileInfoService downloadFileInfoService;

    @Autowired
    public RedisService redisService;

    @Autowired
    public FtpService ftpService;

    /**
     * handler 执行改方法
     */
    @Override
    public void execute(DownParam downParam) {
        XxlJobHelper.log("开始下载");
        // 下载参数
        DownloadFileInfoEntity downLoadInfo = new DownloadFileInfoEntity();
        /** 1. 获取执行参数*/
        TriggerParam triggerParam = XxlJobHelper.getTriggerParam();
        // 下载模板参数
        String forFilePath = null;
        String toFilePath = null;

        long fileSize = 0;
        //包括："原路径＋原名称＋","＋新路径＋新名称＋","+文件大小"+","+文件的时间
        String fileParam[] = triggerParam.getExecutorParams().split(",");
        //源文件的绝对地址
        forFilePath = fileParam[0];
        XxlJobHelper.log("源文件路径："+forFilePath);
        //目标文件的绝对地址
        toFilePath = fileParam[1];
        XxlJobHelper.log("目标文件的绝对地址："+toFilePath);
        //文件的大小
        fileSize = Long.parseLong(fileParam[2]);
        String redisKey = fileParam[4];
        XxlJobHelper.setRedisKey(redisKey);

        try {
            //文件的时间
            long fileDateLong = Long.parseLong(fileParam[3]);
            //文件的时间
            Date fileDate = new Date(fileDateLong);
            downLoadInfo.setDataTime(fileDate);
            downLoadInfo.setFileName(new File(toFilePath).getName());
            this.setRealFileName(downLoadInfo,forFilePath);

            downLoadInfo.setFileSize(fileSize);
            downLoadInfo.setFormat(toFilePath.substring(toFilePath.lastIndexOf(".")));
            //准备下载
            downLoadInfo.setZt("0");
            downLoadInfo.setFilePath(new File(toFilePath).getParent());
            downLoadInfo.setBz(downParam.getForSouceType() + "2" + downParam.getToSouceType());
            // 将准备下载信息入库
            downloadFileInfoService.save(downLoadInfo);

            /** 2.开始下载 */
            Boolean flag = this.down(triggerParam, downLoadInfo, downParam, forFilePath);

            /** 3.下载成功入库*/
            if (flag) {
                downLoadInfo.setZt("1");
                downloadFileInfoService.updateById(downLoadInfo);
            }
            XxlJobHelper.log("下载成功");
        } catch (NumberFormatException e) {
            throw new CommonException("文件名时间为空,"+e.getMessage());
        } catch (Exception e) {
            throw new CommonException("下载出错,"+e.getMessage());
        } finally {
            if (redisService.exists(redisKey)) {
                redisService.remove(redisKey);
            }
        }
        XxlJobHelper.handleResult(ReturnT.SUCCESS_CODE,"调度结束,执行成功");
    }

    @Override
    public ReturnT<List<String>> executeShard(TaskParam taskParam) {
        ReturnT<List<String>> result = new ReturnT<>();
        // 页面模板参数
        String params = taskParam.getModelParameters();
        DownParam downParam = JSON.parseObject(params, DownParam.class);

        /*** ======1.获取开始和结束日期========== ***/
        List<Date> dateRanges = this.getBeginAndEndTime(downParam,taskParam.getDynamicMap());
        Date beginTime = dateRanges.get(0);
        Date endTime = dateRanges.get(1);
        /*** ======2.获取该时间段内数据库中的数据========== ***/
        List<String> existFileList = downloadFileInfoService.findFilesByTime(beginTime, endTime);
        /*** ======3.获取文件符合条件的文件list========== ***/
        List<String> files = this.getFileList(downParam, beginTime, endTime, existFileList);

        log.info("总共需要处理：{} 期数据",files.size());
        result.setData(files);
        return result;
    }


    /**
     * 获取处理文件开始和结束时间
     * @param downParam
     * @param dyMap
     * @return
     */
    private List<Date> getBeginAndEndTime(DownParam downParam,LinkedHashMap dyMap) {
        try {
            // H8 数据汇集减8个小时
            String mark = "";
            if(dyMap.containsKey("mark")){
                mark = (String)dyMap.get("mark");
            }
            Date endTime = new Date();
            Date beginTime = new Date();
            if ("H8".equals(mark)){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(beginTime);
                calendar.add(Calendar.HOUR,-8);
                beginTime =calendar.getTime();
                endTime = calendar.getTime();
            }
            if ("now".equals(downParam.getDownloadType())) {
                int hours = Integer.parseInt(downParam.getDownloadDays());
                beginTime = DateUtil.addHour(endTime, -hours);
            } else if ("history".equals(downParam.getDownloadType())) {
                String[] temp = downParam.getDownloadDate().split(",");
                beginTime = new SimpleDateFormat(DateConstant.YYYY_MM_DD_HH_MM_SS).parse(temp[0]);
                endTime = new SimpleDateFormat(DateConstant.YYYY_MM_DD_HH_MM_SS).parse(temp[1]);
            }
            List<Date> dates = new ArrayList<>();
            dates.add(beginTime);
            dates.add(endTime);
            return dates;
        } catch (Exception e) {
            throw new CommonException("数据开始时间和结束时间获取失败");
        }
    }

    /**
     * 获取文件符合条件的文件list
     * @param downParam
     * @param beginTime
     * @param endTime
     * @param existFileList
     * @return
     */
    private List<String> getFileList(DownParam downParam, Date beginTime, Date endTime, List<String> existFileList){

        // 返回结果包括："原路径＋原名称＋","＋新路径＋新名称＋","+文件大小"
        // 把遍历的目录保存到pathList中 确保目录只遍历一次
        Set<String> pathList = this.getPathList(downParam, beginTime, endTime);
        List<String> fileList;
        try {
            fileList = this.downLoadSourceData(pathList, downParam, beginTime, endTime, existFileList);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonException(e.getMessage());
        }
        return fileList;
    }

    /**
     * 获取符合时间格式的所有文件夹
     *
     * @param downParam
     * @param beginTime
     * @param endTime
     * @return
     */
    private Set<String> getPathList(DownParam downParam, Date beginTime, Date endTime) {
        //源路径
        String formPath = downParam.getForPath();
        //需要处理的路径
        Set<String> pathList = new HashSet<String>();
        //[rain|temp|windS] 将目录中的 yyyyMM dd 替换为时间
        if (formPath.contains("{")) {
            this.addTimeDirToSet(beginTime, endTime, formPath, pathList);
        } else {
            pathList.add(formPath);
        }
        return pathList;
    }

    /**
     * 将路径中 yyyy MM dd 等转换成时间
     * @param beginTime
     * @param endTime
     * @param formPath
     * @param pathList
     */
    private void addTimeDirToSet(Date beginTime, Date endTime, String formPath, Set<String> pathList) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginTime);
        while (calendar.getTime().getTime() <= endTime.getTime()) {
            String forPath = DateUtil.getPathByDate(formPath, calendar.getTime());
            calendar.add(Calendar.HOUR, 1);
            if (pathList.contains(forPath)) {
                continue;
            }
            pathList.add(forPath);
        }
    }

    /**
     * 从 FTP 下载数据到 本地
     * @param downParam
     * @param forFilePath
     * @param toFilePath
     * @param fileSize
     * @return
     */
    public Boolean downLoadFromFtp(DownParam downParam, String forFilePath, String toFilePath, Long fileSize) {
        Boolean flag = true;
        FtpEntity ftp = ftpService.getById(downParam.getForFtp());
        ApacheFtpUtil ftpUtil = new ApacheFtpUtil(ftp);
        if (ftpUtil.connectServer()) {
            long lRemoteSize = ftpUtil.downloadFTP(forFilePath, toFilePath, fileSize);
            ftpUtil.closeServer();
            if (lRemoteSize >= fileSize) {
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 将 数据从本地上传到 FTP
     * @param downParam
     * @param forFilePath
     * @param toFilePath
     * @return
     */
    public Boolean uploadToFtp(DownParam downParam, String forFilePath, String toFilePath) {
        Boolean flag = true;
        FtpEntity ftp = ftpService.getById(downParam.getToFtp());
        ApacheFtpUtil ftpUtil = new ApacheFtpUtil(ftp);
        if (ftpUtil.connectServer()) {
            flag = ftpUtil.uploadFile(forFilePath, toFilePath);
            ftpUtil.closeServer();
        }
        return flag;
    }

    /**
     * 将数据 从本地 上传到sftp
     * @param downParam 模板参数
     * @param toFilePath 目标路径
     * @param forFilePath 源路径
     * @return
     */
    public Boolean uploadToSftp(DownParam downParam, String toFilePath, File forFilePath) {
        FtpEntity sftp = ftpService.getById(downParam.getToFtp());
        SftpUtils toSftpUtil = new SftpUtils(sftp);
        toSftpUtil.connect();
        Boolean flag = toSftpUtil.uploadFile(toFilePath, forFilePath.getAbsolutePath());
        toSftpUtil.disconnect();
        return flag;
    }

    /**
     * 获取临时文件信息
     * @param triggerParam
     * @param toFilePath
     * @return
     */
    public File getTempFile(TriggerParam triggerParam, String toFilePath) {
        // 动态参数指定临时路径，用临时路径做本地存储，没有指定临时路径 用 目标路径
        LinkedHashMap<String, Object> dynamicMap = triggerParam.getTaskParam().getDynamicMap();
        String tempFilePath = (String)dynamicMap.getOrDefault("tempFile", toFilePath);
        return new File(tempFilePath);
    }

}
