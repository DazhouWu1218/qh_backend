package com.htht.executor.task.service.base.impl;


import com.alibaba.fastjson.JSON;
import com.htht.executor.task.service.BaseProductHandlerService;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.entity.xml.InputXmlParamDTO;
import com.htht.job.core.util.FileNameUtils;
import com.htht.job.core.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * VNP 产品调度
 */
@Service(value="vnpHandlerService")
@Slf4j
public class VnpHandlerService extends BaseProductHandlerService {


    @Override
    public String addInputXmlParam(TriggerParam triggerParam, ProductParam productParam, InputXmlParamDTO inputXmlParam) {
        String inputPath = productParam.getInputFilePath();
        String issue = triggerParam.getExecutorParams();
        String filePath = FileNameUtils.dealFilePath(inputPath, issue);
        inputXmlParam.setInputFile(filePath);
        return null;
    }

    @Override
    public ReturnT<List<String>> executeShard(TaskParam taskParam){
        // 获取父类的返回结果 并过滤
        ReturnT<List<String>> listReturnT = super.executeShard(taskParam);
        // 期次集合
        List<String> list = listReturnT.getData();

        // 获取算法参数
        LinkedHashMap<String, Object> fixedMap = taskParam.getFixedMap();

        // 路径集合
        Set<String> pathSet = new HashSet<>();
        fixedMap.forEach((s, o) -> {
            pathSet.add((String)o);
        });
        // 获取算法参数需要进行校验的路径 过滤掉没有文件夹的期次
        List<String> collect = Optional.of(list).orElse(new ArrayList<>())
                .stream()
                .filter(issue -> this.isExitPath(issue, pathSet))
                .collect(Collectors.toList());
        log.info("VNP 需要处理的期次 ====>{}", JSON.toJSONString(collect));
        listReturnT.setData(collect);
        return listReturnT;
    }

    /**
     * 判断路径是否存在
     * @param issue
     * @param pathSet
     * @return
     */
    private boolean isExitPath(String issue, Set<String> pathSet) {
        boolean flag = true;
        String  pathDate = issue.substring(0,8);
        for (String path : pathSet) {
            File file = new File(path,pathDate);
            if (!file.isDirectory()) {
                // 只要有一个文件路径不存在 返回false 并且不处理该期次
                flag = false;
                break;
            }
        }
        return flag;
    }

    @Override
    public LinkedHashMap<String,Object> executeFixMap(String issue){
        LinkedHashMap<String, Object> fixedMap = XxlJobHelper.getTriggerParam().getTaskParam().getFixedMap();
        LinkedHashMap<String, Object> returnMap = new LinkedHashMap<>(6);
        fixedMap.forEach((key,value) -> {
            String path = value + File.separator + issue.substring(0,8);
            String regex = ".*.";
            if (Objects.equals("VNP09A1_M11",key)) {
                regex = ".*.M11_mosaic.tif$";
                pubMapInfo(returnMap, key, path, regex);
            } else if (Objects.equals("VNP21A2_qc_D",key)) {
                regex = ".*.QC_Day_mosaic.tif$";
                pubMapInfo(returnMap, key, path, regex);
            } else if (Objects.equals("VNP09A1_M7",key)) {
                regex = ".*.SurfReflect_M7_mosaic.tif$";
                pubMapInfo(returnMap, key, path, regex);
            }else if (Objects.equals("VNP09A1_M5",key)) {
                regex = ".*.SurfReflect_M5_mosaic.tif$";
                pubMapInfo(returnMap, key, path, regex);
            } else if (Objects.equals("LST",key)) {
                regex = ".*.LST_Day_1KM_mosaic.tif$";
                pubMapInfo(returnMap, key, path, regex);
            }else if (Objects.equals("VNP09A1_qc",key)) {
                regex = ".*.QC_mosaic.tif$";
                pubMapInfo(returnMap, key, path, regex);
            } else {
                returnMap.put(key,value);
            }
        });

        return returnMap;
    }

    private void pubMapInfo(LinkedHashMap<String, Object> returnMap, String key, String path, String regex) {
        List<File> files = FileUtil.iteratorFileAndDirectory(new File(path), regex);
        if (!files.isEmpty()) {
           returnMap.put(key,files.get(0).getPath());
        }
    }

}
