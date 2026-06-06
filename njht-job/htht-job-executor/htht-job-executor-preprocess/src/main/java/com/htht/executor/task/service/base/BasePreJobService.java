package com.htht.executor.task.service.base;

import com.htht.executor.satellite.entity.SateDataInfoEntity;
import com.htht.executor.satellite.service.SateDataTaskFileService;
import com.htht.executor.sys.service.DicService;
import com.htht.executor.task.constant.PreProcessConstant;
import com.htht.executor.task.util.PreJobUtil;
import com.htht.executor.task.util.XmlMakeUtil;
import com.htht.executor.task.util.XmlProjectionUtil;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.entity.paramtemplate.PreDataParam;
import com.htht.job.core.exception.CommonException;
import com.htht.job.core.log.XxlJobFileAppender;
import com.htht.job.core.redis.RedisService;
import com.htht.job.core.util.FileUtil;
import com.htht.job.core.util.ScriptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 代国军
 * @description: 预处理实现
 * 调度过程中需要将 redis key set 到 XxlJobHelper 中, 回调会清理对应的redis信息，防止缓存脏数据
 * @date 2022/5/19 15:52
 */
@Slf4j
@Service
public class BasePreJobService implements PreJobService {

    @Autowired
    protected RedisService redisService;

    @Autowired
    private SateDataTaskFileService sateDataTaskFileService;

    @Autowired
    private DicService dicService;

    @Override
    public void execute(TriggerParam triggerParam) {
        String jobParam = XxlJobHelper.getJobParam();
        // 获取 shard 返回结果信息
        assert jobParam != null;
        String filePath = this.getExecuteFile(jobParam,triggerParam);
        triggerParam.setExecutorParams(filePath);
        String issue = this.getExecuteIssue(jobParam,triggerParam);
        XxlJobHelper.log("原始数据位置："+filePath);
        //模板参数
        PreDataParam preDataParam = PreJobUtil.getPreDataParam(triggerParam.getTaskParam());
        // redisKey
        String redisKey = PreProcessConstant.SATELLITE_REDIS_KEY+preDataParam.getPreDataTaskName()+new File(filePath).getName();
        log.info("redisKey：：{}",redisKey);
        XxlJobHelper.setRedisKey(redisKey);
        try {
            /**1.制作xml，判断数据是否以入库去重*/
            LinkedHashMap<String, Object> dyMap = this.getXmlMapInfo(triggerParam,issue,preDataParam);

            /** 2.调用算法 */
            //状态为false的直接跳过调用算法这一步
            Boolean flag = (Boolean) dyMap.get("status");
            if(flag){
                String exePath = preDataParam.getProjectionExeLocaiton();
                XxlJobHelper.log("算法路径为："+exePath);
                String inputXmlPath= (String) dyMap.get("inputXmlPath");
                XxlJobHelper.log("开始调用算法");
//                SystemCommandUtil.execute(exePath,inputXmlPath);
                int timeout = triggerParam.getExecutorTimeout() == 0 ? 2 * 60 * 60 : triggerParam.getExecutorTimeout();
                ScriptUtil.execute(exePath,inputXmlPath,String.valueOf(timeout));
            }
            /** 3.解析输出xml */
            Map<String, String> returnMap;
            String outputXml = (String)dyMap.get("outPutXmlPath");
            XxlJobHelper.log("输出xml路径:{}",outputXml);
            XxlJobHelper.log("开始解析输出xml");
            if (outputXml == null || !new File(outputXml).exists()) {
                log.error("satellite handler execute error,输出文件不存在,对应路径：{}",outputXml);
                XxlJobHelper.log("算法调用失败,输出文件不存在,对应路径：{}",outputXml);
                throw new CommonException("输出文件不存在");
            }
            // 输出xml 存在 继续往下执行
            returnMap = XmlProjectionUtil.getProjection(outputXml);
            //输出xml中用于判断是否成功
            String successInfo  = returnMap.get("loglevel");
            String successMsg = returnMap.get("loginfo");
            log.info("loginfo:{}",successMsg);
            if(successInfo.contains("info") && successMsg.contains("成功")){
                /** 4.入库   0不入库 1入库*/
                if (!PreProcessConstant.ZERO.equals(preDataParam.getIsInsert())){
                    sateDataTaskFileService.savePreDataInfo(preDataParam,dyMap,returnMap,triggerParam,dicService.findSateUrl());
                }
                XxlJobHelper.handleSuccess("预处理成功");
            } else {
                log.error("satellite handler execute error,算法调度结果报错,对应输出文件路径：{}",outputXml);
                XxlJobHelper.log("算法调用失败,输出文件结果报错,对应路径：{}",outputXml);
                throw new CommonException("算法调用失败");
            }
        } catch (Exception e) {
            throw new CommonException("预处理出错："+e.getMessage());
        } finally {
            // 删除redis
            if(redisService.exists(redisKey)){
                redisService.remove(redisKey);
            }
        }
    }


    @Override
    public String getExecuteFile(String jobParam, TriggerParam triggerParam) {
        return jobParam.substring(jobParam.lastIndexOf("#")+1);
    }

    @Override
    public String getExecuteIssue(String jobParam, TriggerParam triggerParam) {
        return jobParam.substring(0,jobParam.lastIndexOf("#"));
    }

    /**
     * 获取需要制作xml的信息
     * @param triggerParam
     * @param issue
     * @return
     */
    private LinkedHashMap<String,Object> getXmlMapInfo(TriggerParam triggerParam, String issue,PreDataParam preDataParam) throws Exception {
        // 任务参数
        TaskParam taskParam = triggerParam.getTaskParam();
        //输入参数
        LinkedHashMap<String, Object> dyMap = taskParam.getDynamicMap();
        //原始数据路径
        String targetFilePath = triggerParam.getExecutorParams();
        // 可能存在多个文件的情况 命名用第一个
        String[] split = targetFilePath.split(",");
        File file = new File(split[0]);
        String fileName = file.getName();
        final long count = sateDataTaskFileService.selectCountByFileName(fileName);
        if(count != 0){
            throw new CommonException("产品已入库");
        }
        if(issue != null){
            issue =  issue.substring(0,8);
        }else {
            throw new CommonException("匹配文件期次失败");
        }
        dyMap.put("issue",issue);
        //任务名称格式为 卫星_传感器_分辨率
        SateDataInfoEntity sateDataInfoEntity = PreJobUtil.setSatelliteInfo(preDataParam);
        String satellite = sateDataInfoEntity.getSatelliteId();
        String sensor = sateDataInfoEntity.getSensorId();
        String resolution = String.valueOf(sateDataInfoEntity.getResolution());

        //将入库可能用到的信息都放到map中
        dyMap.put("satellite",satellite);
        dyMap.put("sensor",sensor);
        dyMap.put("resolution",resolution);
        dyMap.put("targetFilePath",targetFilePath);

        // inputXml路径
        String inputXmlPath = preDataParam.getProjectionInputArgXml();
        // 输出文件的路径
        String outputDir = preDataParam.getOutputDataFilePath();

        inputXmlPath = inputXmlPath+File.separator+satellite+File.separator+sensor+File.separator+issue.substring(0,6)
                +File.separator+issue+File.separator+fileName.substring(0,fileName.lastIndexOf("."))+".xml";

        outputDir = FileUtil.getFilePath(outputDir,issue);
        //创建inputXml文件
        dyMap.put("inputXmlPath",inputXmlPath);
        //不需要调算法的跳过制作xml这一步
        String algorithm = (String)dyMap.get("algorithm");
        if(algorithm.equals(PreProcessConstant.ZERO)){
            //状态区分用不用调算法
            dyMap.put("status",false);
            //获取输出xml信息
            String outputXml = (String)dyMap.get("outPutXmlPath");
            String outPutXmlPath = this.getOutPutXmlPath(outputXml, targetFilePath,issue);
            dyMap.put("outPutXmlPath",outPutXmlPath);
            return dyMap;
        }
        String outPutXmlPath = outputDir +File.separator + fileName + ".xml";
        dyMap.put("outPutXmlPath",outPutXmlPath);
        XxlJobFileAppender.makeFileNameByPath(inputXmlPath);
        //把需要制作xml的信息添加到map中去制作xml
        Map<String, Object> argMap = this.paramProjection(targetFilePath, outputDir, preDataParam);
        //制作xml
        XxlJobHelper.log("生成输入xml,路径为:"+inputXmlPath);
        XmlMakeUtil.makeXml(argMap,inputXmlPath);
        dyMap.put("status",true);
        return dyMap;
    }

    /**
     * 根据原始数据匹配到对应的数据输出xml信息
     * @param issue
     * @param outputXml
     * @param targetFilePath
     */
    private String getOutPutXmlPath(String outputXml, String targetFilePath,String issue) {

        String filePath = FileUtil.getFilePath(outputXml,issue);
        String regex1 = ".*.xml$";
        Matcher matcher = Pattern.compile(regex1).matcher(filePath);
        String name = new File(targetFilePath).getName();
        String regex =name.substring(0,name.lastIndexOf("."));
        if(matcher.find()){
            File file = new File(filePath);
            if(file.getName().contains(name)){
                return filePath;
            }else {
                return null;
            }
        }
        List<File> subFiles = FileUtil.iteratorFileAndDirectory(filePath, regex);
        if(subFiles.isEmpty()){
            return null;
        }
        XxlJobHelper.log("输出文件路径:"+subFiles.get(0).getPath());
        return subFiles.get(0).getPath();
    }

    /**
     * 需要制作xml的参数
     * @param inputFilePath
     * @param outputDir
     * @param preDataParam
     * @return
     */
    private Map<String, Object> paramProjection(String inputFilePath, String outputDir, PreDataParam preDataParam) {
        Map<String, Object> argMap = new HashMap<>();
        argMap.put("InputFilename", inputFilePath);
        if (StringUtils.isNotEmpty(preDataParam.getSslRid()) && !PreProcessConstant.ZERO.equals(preDataParam.getIsManyInput())) {
            argMap.put("SSLRID",preDataParam.getSslRid());
        }
        argMap.put("ExtArgs", preDataParam.getExtArgs());
        argMap.put("OutputDir", outputDir);
        argMap.put("Bands", preDataParam.getBands());
        argMap.put("ProjectionIdentify",
                preDataParam.getProjectionIdentify() == null || "".equals(preDataParam.getProjectionIdentify())
                        || "none".equals(preDataParam.getProjectionIdentify()) ? "GLL"
                        : preDataParam.getProjectionIdentify());

        argMap.put("ValidEnvelopes", new String[] { preDataParam.getValidEnvelopes() });
        argMap.put("Envelopes", new String[] { preDataParam.getEnvelopes() });
        argMap.put("Formate", preDataParam.getFormate());
        argMap.put("ResolutionX", preDataParam.getResolutionX());
        argMap.put("ResolutionY", preDataParam.getResolutionY());
        argMap.put("PervObservationDate", "");
        argMap.put("PervObservationTime", "");
        argMap.put("OrbitIdentify", "");
        return argMap;
    }

}
