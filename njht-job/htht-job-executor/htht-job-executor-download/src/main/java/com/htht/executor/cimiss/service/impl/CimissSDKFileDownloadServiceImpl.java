package com.htht.executor.cimiss.service.impl;

import cma.music.utils.HttpDownLoad;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.htht.executor.cimiss.bean.DownloadInfo;
import com.htht.executor.cimiss.bean.ResultBean;
import com.htht.executor.cimiss.service.CimissDownService;
import com.htht.executor.download.entity.DownloadFileInfoEntity;
import com.htht.executor.download.service.DownloadFileInfoService;
import com.htht.job.core.constant.DateConstant;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.entity.paramtemplate.CimissDownParam;
import com.htht.job.core.exception.CommonException;
import com.htht.job.core.redis.RedisService;
import com.htht.job.core.util.DateUtil;
import com.htht.job.core.util.FileNameUtils;
import com.htht.job.core.util.FileUtil;
import com.htht.job.core.util.GsonTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author daiguojun
 * @date 2022-08-09 10:09
 * 文件信息下载
 */
@Slf4j
@Service("cimissSDKFileDownloadService")
public class CimissSDKFileDownloadServiceImpl extends CimissDownService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private DownloadFileInfoService downloadFileInfoService;

    private static final String REDIS_KEY = "CIMISS_FILE_DOWNLOAD::";

    @Override
    public void execute(CimissDownParam cimissParam, StringBuffer retStr) {
        // 获取文件标识  以及文件时间正则  用于生成文件存储路径
        LinkedHashMap<String, Object> dynamicMap = XxlJobHelper.getTriggerParam().getTaskParam().getDynamicMap();
        String fileRegex = (String)dynamicMap.get("fileRegex");
        String fileMark = (String)dynamicMap.get("fileMark");
        final String redisKey = REDIS_KEY + fileMark;
        try {

            // 文件存储路径
            String savePath = cimissParam.getFilePath();
            // 接口返回结果
//            String filePath = "D:\\htht\\1.json";
//            File file = new File(filePath);
//            String s = JsonUtil.readJsonFile(file);
            ResultBean resultBean = getFileOrStationData(retStr.toString());
//            ResultBean resultBean = getFileOrStationData(s);
            @SuppressWarnings("unchecked")
            List<DownloadInfo> fileInfoList = resultBean.getData();

            /** 下载文件 数据库 + redis 去重*/
            // 1.过滤掉已经入库的数据
            List<DownloadInfo> filterFileParamList = downloadFileInfoService.filterCimissFileList(fileInfoList,cimissParam);

            // 2.下载文件,下载过程以及redis去重，防止重复下载
            Optional.ofNullable(filterFileParamList).orElse(new ArrayList<>())
                    .forEach(param -> {
                        this.downLoadFile(param,savePath,fileRegex,redisKey);
                    });
            // 3.下载成功,将redis中状态为成功的数据入库,(下载失败的数据,下次定时任务在下载)
            downloadFileInfoService.saveListInfo(this.redisList(redisKey));
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        } finally {
            if (redisService.exists(redisKey)) {
                redisService.remove(redisKey);
            }
        }
    }

    /**
     * 文件下载 （依据redis 去重）
     * 1.key is not exist 返回一个新集合,key is exist 返回value
     * 2.依据当前数据信息生成downLoadInfoEntity对象
     * 3.判断list中是否包含当前数据，包含该数据下载中，或下载完成
     * 4.redis 不包含该数据，更新redis list 并下载，下载成功之后再次更新redisList 修改数据状态为success
     * 5.下载完成 状态为成功的数据入库
     * @param param
     * @param savePath
     * @param fileRegex
     */
    private void downLoadFile(DownloadInfo param, String savePath, String fileRegex,String redisKey) {
        // 添加redis key 用于任务回调之后删除缓存
        XxlJobHelper.setRedisKey(redisKey);
        DownloadFileInfoEntity entity = new DownloadFileInfoEntity();
        entity.setBz("cimiss-file");
        //下载路径
        String fileUrl = param.getFileURL();
        // 文件名
        String filename = param.getFileName().substring(param.getFileName().indexOf("/")+1);
        entity.setFileName(filename);
        // 文件存储路径拼接时间
        savePath = this.getSavePath(entity,savePath,filename,fileRegex);
        File file = new File(savePath,filename);
        // 父目录不存在创建目录
        try {
            FileUtils.forceMkdirParent(file);
        } catch (IOException e) {
            throw new CommonException(e.getMessage());
        }

        entity.setRealFileName(FileNameUtils.getFileRealName(file));
        entity.setFilePath(file.getPath());
        entity.setFileSize(Long.valueOf(param.getFileSize()));
        entity.setFormat(FileNameUtils.getFileSuffix(file));
        // 默认下载中，成功之后状态修改为1 最后只入库状态为1的数据
        entity.setZt("0");
        // 获取redis 中的数据信息
        List<DownloadFileInfoEntity> redisList = this.redisList(redisKey);

        // 判断redis中是否包含当前数据下载任务
        List<DownloadFileInfoEntity> currentInfoList = redisList.stream().filter(item -> Objects.equals(filename, item.getFileName())).collect(Collectors.toList());
        // 不包含 则开始下载
        if( currentInfoList.isEmpty() ){
            redisList.add(entity);
            // 更新缓存并下载
            redisService.set(redisKey, JSONObject.toJSONString(redisList));
            // 下载文件
            XxlJobHelper.log(filename+"开始下载");
            if (HttpDownLoad.httpDownload(fileUrl,file.getPath())) {
                // 下载成功,修改状态并更新redis
                XxlJobHelper.log(filename+"下载成功");
                entity.setZt("1");
                List<DownloadFileInfoEntity> successList = this.redisList(redisKey);
                successList.add(entity);
                redisService.set(redisKey,GsonTool.toJson(successList));
            } else {
                FileUtil.deleteFile(file);
                XxlJobHelper.log(filename+"下载失败,失败url："+fileUrl);
            }
        }
    }

    /**
     * 从redis 中获取当前集合 没有当前可以则创建一个list
     * @return
     */
    private List<DownloadFileInfoEntity> redisList (String redisKey){

        if (!redisService.exists(redisKey)) {
            return new ArrayList<>();
        }
        String json = (String) redisService.get(redisKey);
        return JSON.parseArray(json, DownloadFileInfoEntity.class);
    }

    /**
     * 处理文件保存路径 filepath/yyyyMM/yyyyMMdd
     *
     * @param entity
     * @param savePath
     * @param filename
     * @param fileRegex
     * @return
     */
    private String getSavePath(DownloadFileInfoEntity entity, String savePath, String filename, String fileRegex) {
        StringBuffer stringBuffer = new StringBuffer();
        String fileTime = FileUtil.getFileTime(fileRegex, filename);
        if (StringUtils.isEmpty(fileTime)) {
            throw new CommonException("文件名匹配时间失败:"+filename);
        }
        stringBuffer.append(savePath);
        if (!(savePath.endsWith("/") || savePath.endsWith("\\"))) {
            stringBuffer.append(File.separator);
        }
        String filePath = FileUtil.getFilePath(savePath, fileTime);

        // 添加数据时间到对象中
        Date date = DateUtil.strToDate(fileTime,DateConstant.YYYYMMDDHHMMSS);
        entity.setDataTime(date);
        return filePath;
    }

    @Override
    public String getCurrentDay(String times, String timeKey) throws ParseException {
        // 文件下载一小时下载一次,每小时下载前一个小时的数据
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,-1);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        return DateUtil.dateToStr(calendar.getTime(), DateConstant.YYYYMMDDHHMMSS);

    }

    @Override
    public void setTimes(HashMap<String, String> map, String times) {
        map.put("time", times);
    }

}
