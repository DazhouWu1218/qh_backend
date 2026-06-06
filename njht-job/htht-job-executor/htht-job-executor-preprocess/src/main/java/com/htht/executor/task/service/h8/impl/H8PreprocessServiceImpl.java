package com.htht.executor.task.service.h8.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htht.executor.satellite.service.SateDataInfoService;
import com.htht.executor.task.constant.PreProcessConstant;
import com.htht.executor.task.param.H8PreParam;
import com.htht.executor.task.service.h8.H8PreprocessService;
import com.htht.executor.task.util.H8FileOperate;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.constant.DateConstant;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.entity.xml.XmlDTO;
import com.htht.job.core.exception.CommonException;
import com.htht.job.core.redis.RedisService;
import com.htht.job.core.util.*;
import com.htht.job.core.xml.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 代国军
 * @description: h8预处理
 * @date 2022/5/24 15:11
 */
@Service
@Slf4j
public class H8PreprocessServiceImpl implements H8PreprocessService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private SateDataInfoService sateDataInfoService;

    @Override
    public void execute(TriggerParam triggerParam) {
        /** 1.获取参数列表 **/
        TaskParam taskParam = triggerParam.getTaskParam();
        LinkedHashMap<String, Object> fixedMap = taskParam.getFixedMap();
        /** 2.解析产品参数 **/
        H8PreParam preParam = JSON.parseObject(taskParam.getModelParameters(), H8PreParam.class);
        String exePath = preParam.getExePath();
        String redisKey = "";
        String issue = "";
        // 制作输入xml
        String paramStr = triggerParam.getExecutorParams();
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> inputXmlParam = gson.fromJson(paramStr, type);
        try {
            inputXmlParam.put("outLogPath", XxlJobHelper.getJobLogFileName());
            issue = inputXmlParam.get("fileTime");
            log.info(">>>>> 当前期次：{}",issue);
            XxlJobHelper.log("开始执行,需要处理的期次是："+issue);
            redisKey = inputXmlParam.remove("redisKey");
            XxlJobHelper.setRedisKey(redisKey);
            String outputXml = inputXmlParam.get("outXMLPath");
            String inputXml = inputXmlParam.remove("inputXml");
//            String originFileABPath = inputXmlParam.get("originFilePath");
            List<XmlDTO> inputList = this.formatXmlParam(inputXmlParam);
            XxlJobHelper.log("输入xml路径为："+inputXml);
            XmlUtils.createAlgorithmXml(preParam.getIdentify(), inputList, new ArrayList<>(), inputXml);
            /** =======4.执行脚本=========== **/
            XxlJobHelper.log("H8预处理正在执行算法："+exePath);
            int executorTimeout = XxlJobHelper.getTriggerParam().getExecutorTimeout();
            ScriptUtil.execute(exePath,inputXml,String.valueOf(executorTimeout));
            /** ========5.脚本 结束======= **/
            File outputXmlFile = new File(outputXml);
            if (!outputXmlFile.exists()) {
                XxlJobHelper.log("outputXmlFile文件不存在，路径为：" + outputXml);
                throw new CommonException("outputXmlFile文件不存在");
            }

            XxlJobHelper.log("开始读取输出xml文件" + outputXml);
            int b = XmlUtils.isFireSuccessByXml(outputXml);
            if (b == 0) {
                XxlJobHelper.log("xml读取失败");
                throw new CommonException("算法调度失败");
            }
//            XxlJobHelper.log("算法执行成功，准备入库");

            // 入库
//            if (b == 1) {
//                // 字典表 url 用于生成数据绝对路径
//                String satelliteUrl = dicService.findSateUrl();
//                sateDataTaskFileService.saveH8ProjectToDB(originFileABPath,outputXml,issue,triggerParam,satelliteUrl);
//            }

//            XxlJobHelper.log("入库成功,执行结束");
            XxlJobHelper.handleSuccess("H8预处理成功");
        } catch (Exception e) {
            throw new CommonException("H8预处理异常："+e.getMessage());

        } finally {
            if (redisService.exists(redisKey)) {
                redisService.remove(redisKey);
            }
        }

    }
   
    /**
     * 将参数放到xml中
     * @param map
     * @return
     */
    private List<XmlDTO> formatXmlParam(Map<String, String> map) {
        List<XmlDTO> inputList = new ArrayList<>();
        map.forEach((key, value) -> {
            XmlDTO inputFileXmlDTO = new XmlDTO();
            inputFileXmlDTO.setIdentify(key);
            inputFileXmlDTO.setValue(value);
            inputFileXmlDTO.setDescription(" ");
            inputFileXmlDTO.setType("string");
            inputList.add(inputFileXmlDTO);
        });
        return inputList;
    }

    @Override
    public ReturnT<List<String>> executeShard(TaskParam taskParam) {
        ReturnT<List<String>> returnT = new ReturnT<>();
        String param = taskParam.getModelParameters();
        LinkedHashMap<String, Object> fixedMap = taskParam.getFixedMap();
        String projectKey = PreProcessConstant.H8_SATELLITE_REDIS_KEY;
        List<String> list = new ArrayList<String>();
        String redisKey = "";
        try {
            //将json格式的params转换为ProductParam类型
            H8PreParam preParam = JSON.parseObject(param, H8PreParam.class);
            /** 解析产品参数 **/
            String xmlPath = preParam.getXmlPath();
            String inputPath = preParam.getInputPath();
            String outputPath = preParam.getOutFolder();
            String fileNamePattern = preParam.getFileNamePattern();
            // 根据周期，实时，历史，处理期次 获取要处理的期次集合
            List<String> issueList = this.getTimeList(preParam);

            for(String issue:issueList) {
                redisKey = projectKey + ":" + issue + outputPath;
                log.info("======H8::数据预处理:: 当前期次：{}=======", issue);
                if (redisService.exists(redisKey)) {
                    // 时间增加10分钟
                    continue;
                }
//                //判断该期次是否执行过了,没执行过返回false
//                if (Boolean.TRUE.equals(this.isExistProjectData(issue))) {
//                    log.info("======H8::数据预处理 isExistProjectData 数据库以存在，当前期次为 ：{}=======", issue);
//                    continue;
//                }
                String y = issue.substring(0,4);
                String ym = issue.substring(0, 6);
                String ymd = issue.substring(0, 8);
                //sample:_H8Fire_SC
                String outputLogOrXmlName = "_" + projectKey;
                String commonPath = "/H8/AHI/" + y+File.separator+ym+File.separator+ymd + File.separator;
                String inputXml = xmlPath + commonPath + issue + outputLogOrXmlName + "_input.xml";
                String outputXml = xmlPath + commonPath + issue + outputLogOrXmlName + "_output.xml";
                H8FileOperate.newParentFolder(inputXml);
                H8FileOperate.newParentFolder(outputPath);
                H8FileOperate.newParentFolder(outputXml);
                // 制作xml参数
                Map<String, Object> inputXmlParam = new HashMap<>(16);

                inputXmlParam.put("fileTime", issue);
                inputXmlParam.put("inputPath", inputPath);
                inputXmlParam.put("outFolder", outputPath);
                inputXmlParam.put("outXMLPath", outputXml);
                inputXmlParam.put("inputXml", inputXml);

                inputXmlParam.put("extend", fixedMap.get("extend"));
                inputXmlParam.put("channals", fixedMap.get("channals"));
                inputXmlParam.put("pixSize", fixedMap.get("pixSize"));
                inputXmlParam.put("segMents", fixedMap.get("segMents"));
                inputXmlParam.put("specialOutFolder", fixedMap.get("specialOutFolder"));
                inputXmlParam.put("redisKey", redisKey);

                Gson gson = new Gson();
                String str = gson.toJson(inputXmlParam);
                if (!list.contains(str)) {
                    list.add(str);
                }
                redisService.set(redisKey, redisKey);
            }
        } catch (Exception e) {
            throw new CommonException("分片执行异常："+e.getMessage());
        }
        if (list.isEmpty()) {
            returnT.setMsg("没有数据");
            returnT.setCode(ReturnT.FAIL_CODE);
        } else {
            returnT.setCode(ReturnT.SUCCESS_CODE);
            returnT.setData(list);
        }
        return returnT;
    }

    public static final String NOW = "now";
    public static final String HISTORY = "history";

    /**
     * 获取期次集合
     * @param preParam
     * @return
     */
    private List<String> getTimeList(H8PreParam preParam) {
        List<String> issues = new ArrayList<>();

        String dateType = preParam.getDateType();
        if(NOW.equals(dateType)){
            String rangeDay = preParam.getRangeDay();
            // 默认处理最近1小时的数据
            Integer i = Integer.valueOf(StringUtils.isEmpty(rangeDay)?"2":rangeDay);
            Calendar calendar = Calendar.getInstance();
            // H8世界时 时间 -8
            calendar.add(Calendar.HOUR_OF_DAY,-8);
            // 数据有20分钟延迟
            calendar.add(Calendar.MINUTE,-10);
            for(int j=0;j<i*10;j=j+10){
                calendar.add(Calendar.MINUTE, -10);
                String issue = MatchTime.matchIssue(calendar.getTime(), preParam.getCycle());
                issues.add(issue);
            }
        }
        if(HISTORY.equals(dateType)){
            String[] temp = preParam.getTimeRange().split(",");
            Date beginTime = DateUtil.strToDate(temp[0], DateConstant.YYYY_MM_DD_HH_MM_SS);
            Date endTime = DateUtil.strToDate(temp[1], DateConstant.YYYY_MM_DD_HH_MM_SS);
            Calendar cal = Calendar.getInstance();
            cal.setTime(beginTime);
            for (long d = cal.getTimeInMillis(); d <= endTime.getTime(); d = MatchTime.getMinDateToMillis(cal)) {
                Date date = new Date(d);
                String s = MatchTime.matchIssue(date, preParam.getCycle());
                issues.add(s);
            }
        }
        return issues;
    }

    /**
     * 判断该数据是否已入库
     * @param issue
     * @return
     */
    public Boolean isExistProjectData(String issue) {
        Boolean flag = true;
        String satelliteId = "H8";
        String sensorId = "AHI";
        final Date date = DateUtil.strToDate(issue, DateConstant.YYYYMMDDHHMM);
        String day = DateFormatUtils.format(date, DateConstant.YYYY_MM_DD);
        String hour = DateFormatUtils.format(date, DateConstant.HH_MM_SS);

        // 查询该数据是否已入库
        Integer count = sateDataInfoService.selectCountSate(satelliteId, sensorId, day, hour);
        if(count == 0){
            flag = false;
        }
        return flag;
    }

    /**
     * 判断文件个数
     * @param fileDir
     * @param fileNamePattern
     * @param issue
     * @return
     */
    private String checkFileNum(File fileDir, String fileNamePattern, String issue) {
        if(fileNamePattern == null){
            return null;
        }
        String s = fileNamePattern.replace("yyyyMMdd", issue.substring(0, 8));
        String regex = s.replace("HHmm", issue.substring(8, 12)).replace("{","").replace("}","");
        List<File> subFiles = FileUtil.iteratorFileAndDirectory(fileDir.getPath(), regex);
        log.info("==========H8预处理,对应期次[{}],扫描到[{}]个文件==========",issue,subFiles.size());
        if(subFiles.size()==32){
            for(File file:subFiles){
                long length = file.length();
                if(length == 0){
                    log.info("存在文件大小为0 的期次，暂时不进行预处理");
                    return null;
                }
            }
            return subFiles.stream().map(File::getPath).sorted().collect(Collectors.toList()).get(0);
        }else {
            log.info("当前期次{}下载文件个数不够",issue);
            return null;
        }
    }
}
