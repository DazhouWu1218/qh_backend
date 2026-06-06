package com.htht.executor.download.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.executor.cimiss.bean.DownloadInfo;
import com.htht.executor.download.dao.DownloadFileInfoDao;
import com.htht.executor.download.entity.DownloadFileInfoEntity;
import com.htht.executor.download.service.DownloadFileInfoService;
import com.htht.job.core.constant.DateConstant;
import com.htht.job.core.entity.paramtemplate.CimissDownParam;
import com.htht.job.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据下载表
 * @author daiguojun
 * @date 2022-05-17 16:41:22
 */
@Service("downloadFileInfoService")
@Slf4j
public class DownloadFileInfoServiceImpl extends ServiceImpl<DownloadFileInfoDao, DownloadFileInfoEntity> implements DownloadFileInfoService {


    @Override
    public List<String> findFilesByTime(Date beginTime, Date endTime) {
        // 查询在 开始结束时间范围内 且 状态为1 的数据
        List<DownloadFileInfoEntity> fileInfoEntities = baseMapper.selectFileListByTime(beginTime,endTime);
        // 获取文件唯一标识 并返回
        return Optional.ofNullable(fileInfoEntities).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    // 将 路径中的 / 或者 \\ 替换为 # 作为唯一标识，并与文件名拼接
                    String filePath = item.getFilePath().replace("\\", "#").replace("/", "#");
                    return filePath + "#" + item.getFileName();
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delByFileName(String downloadFileName) {
        baseMapper.deleteByFileName(downloadFileName);
    }

    @Override
    public List<DownloadInfo> filterCimissFileList(List<DownloadInfo> fileParamList, CimissDownParam downParam) {
        // 默认下载当天的数据 基于times 下载
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        Date beginTime = calendar.getTime();
        // 结束时间 当天
        calendar.add(Calendar.DAY_OF_YEAR,1);
        calendar.add(Calendar.SECOND,-1);
        Date endTime = calendar.getTime();
        // 历史数据根据时间段下载
        if (downParam.getInterfaceId().contains("TimeRange")) {
            // 获取下载的时间段
            String timeRange = downParam.getTimeRange();
            List<String> timeList = JSON.parseArray(timeRange, String.class);
            beginTime = DateUtil.strToDate(timeList.get(0), DateConstant.YYYYMMDDHHMMSS);
            endTime = DateUtil.strToDate(timeList.get(1),DateConstant.YYYYMMDDHHMMSS);
        }

        List<String> dbFileNameList =  baseMapper.selectFileNameListByTime(beginTime,endTime, "cimiss-file");

        if (dbFileNameList.isEmpty()) {
            return fileParamList;
        }


        // 过滤掉已经入库的数据信息
        return Optional.ofNullable(fileParamList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> !dbFileNameList.contains(item.getFileName().substring(item.getFileName().indexOf("/")+1)))
                .collect(Collectors.toList());

    }

    @Override
    public void saveListInfo(List<DownloadFileInfoEntity> downLoadList) {
        log.info("downLoadList::size() {}",downLoadList.size());
        // 过滤出状态为成功的数据
        List<DownloadFileInfoEntity> collect = Optional.ofNullable(downLoadList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> Objects.equals("1", item.getZt()))
                .collect(Collectors.toList());
        // 入库
        log.info("collect ::size() {}",downLoadList.size());
        this.saveBatch(collect);
    }
}