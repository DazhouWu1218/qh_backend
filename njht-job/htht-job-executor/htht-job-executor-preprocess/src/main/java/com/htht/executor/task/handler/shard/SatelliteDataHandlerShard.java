package com.htht.executor.task.handler.shard;

import com.htht.executor.satellite.service.SateDataTaskFileService;
import com.htht.executor.task.constant.PreProcessConstant;
import com.htht.executor.task.util.PreJobUtil;
import com.htht.executor.task.util.ShardUtil;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.entity.paramtemplate.PreDataParam;
import com.htht.job.core.redis.RedisService;
import com.htht.job.core.shard.SharingHandler;
import com.htht.job.core.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: 代国军
 * @CreateDate: 2021/5/19 9:05
 * @Description: 卫星数据入库
 * shard中进行分片处理 实时
 */
@Service("satelliteDataHandlerShard")
@Slf4j
public class SatelliteDataHandlerShard implements SharingHandler {

    @Autowired
    private SateDataTaskFileService taskFileService;

    @Autowired
    private RedisService redisService;

    /**
     * 查询预处理的文件，并将预处理的文件提交给调度中心
     * @param taskParam
     * @return
     */
    @Override
    public ReturnT<List<String>> executeShard(TaskParam taskParam) {
        log.info("exec satellite date preprocess handler shard");
        ReturnT<List<String>> result = new ReturnT<>();
        List<String> returnList = new ArrayList<>();
        PreDataParam preDataParam = PreJobUtil.getPreDataParam(taskParam);
        // redis 名称
        String taskName = PreProcessConstant.SATELLITE_REDIS_KEY + preDataParam.getPreDataTaskName();
        // 获取时间段 要处理几天的数据
        List<String> issueList = ShardUtil.getIssueList(preDataParam);
        // 查询以入库的文件信息 并过滤掉 (数据不入库时不进行校验)
        List<String> dbFileNameList;
        if (!PreProcessConstant.ZERO.equals(preDataParam.getIsInsert())) {
            dbFileNameList = taskFileService.selectFileNameBySatellite(preDataParam);
            log.info("已入库文件个数:{}",dbFileNameList.size());
        } else {
            dbFileNameList = new ArrayList<>();
            log.info("不进行数据库文件校验>>>>>>>");
        }
        // 日期循环 匹配要入库的数据信息
        for (String time : issueList) {
            //原始数据目录
            String targetFilePath = preDataParam.getInputDataFilePath();
            String filePath = FileUtil.getFilePath(targetFilePath, time);
            //正则
            String regex = preDataParam.getFileNamePattern();
            regex = FileUtil.getFilePath(regex, time);
            log.info("file_path:{},regex:{}",filePath,regex);
            List<File> subFiles = FileUtil.iteratorFileAndDirectory(new File(filePath), regex);
            // 输入是否为多个文件 0否 1是
            if ( !PreProcessConstant.ZERO.equals(preDataParam.getIsManyInput()) ) {
                String files = Optional.ofNullable(subFiles).orElse(new ArrayList<>())
                        .stream()
                        .map(File::getPath)
                        .collect(Collectors.joining(","));
                if (!StringUtils.isEmpty(files) ) {
                    returnList.add(time + "#" +files);
                }
            } else {
                //实时 处理几小时之内的文件,过滤掉已入库以及redis数据
                List<String> collect = Optional.of(subFiles).orElse(new ArrayList<>())
                        .stream()
                        .filter(o -> this.isExecuteFile(o, dbFileNameList, taskName))
                        .map(file -> this.getReturnInfo(file, preDataParam.getFileDatePattern()))
                        .collect(Collectors.toList());
                log.info("当前期次issue:[{}],总共要处理:[{}]期数据",time,collect.size());
                returnList.addAll(collect);
            }
        }
        result.setCode(ReturnT.SUCCESS_CODE);
        result.setData(returnList);
        log.info("exit shard,总共需要处理[{}]期数据===============",returnList.size());
        return result;
    }



    /**
     * 获取shard 返回信息
     * @param file
     * @param fileDatePattern
     * @return
     */
    private String getReturnInfo(File file, String fileDatePattern) {
        String path = file.getPath();
        String issue = FileUtil.getFileTime(fileDatePattern, file.getName());
        return issue + "#" +path;
    }


    /**
     * 获取需要执行的文件信息
     * @param file
     * @param dbFileNameList
     * @param taskName
     * @return
     */
    private boolean isExecuteFile(File file, List<String> dbFileNameList,String taskName) {
        String redisKey = taskName + file.getName();
        log.info("shard redisKey：：{}",redisKey);
        //redis 去重
        if (redisService.exists(redisKey)) {
            // redis包含该信息，过滤掉该条信息
            return false;
        }
        // db 去重
        boolean b = dbFileNameList.contains(file.getName());
        if (b) {
            // 数据库包含该条信息 过滤掉
            return false;
        } else {
            redisService.set(redisKey, file.getPath());
            return true;
        }
    }



}
