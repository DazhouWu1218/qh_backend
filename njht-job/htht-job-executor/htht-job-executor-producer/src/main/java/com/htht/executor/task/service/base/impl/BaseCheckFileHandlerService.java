package com.htht.executor.task.service.base.impl;

import com.alibaba.fastjson.JSON;
import com.htht.executor.product.service.ProductInfoService;
import com.htht.executor.task.constant.BaseProductServiceConstant;
import com.htht.executor.task.service.BaseProductHandlerService;
import com.htht.executor.task.util.ShardUtil;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.constant.DateConstant;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.entity.xml.InputXmlParamDTO;
import com.htht.job.core.util.DateUtil;
import com.htht.job.core.util.FileNameUtils;
import com.htht.job.core.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: 代国军
 * @CreateDate: 2021/10/26 16:35
 * @Description: 文件期次通过扫描文件中获取
 */
@Service("baseCheckFileHandlerService")
@Slf4j
public class BaseCheckFileHandlerService extends BaseProductHandlerService {

    @Autowired
    private ProductInfoService productInfoService;


    @Override
    public String addInputXmlParam(TriggerParam triggerParam, ProductParam productParam, InputXmlParamDTO inputXmlParam) {
        String inputFilePath = triggerParam.getExecutorParams();
        //文件输入路径直接从shard中传递的参数中拿
        inputFilePath = inputFilePath.substring(0,inputFilePath.indexOf("#"));
        File inputF = new File(inputFilePath);
        if(null==inputF){
            return BaseProductServiceConstant.No_Input_Msg + ":" + inputF;
        }
        inputXmlParam.setInputFile(inputF.getPath());
        return null;
    }

    /**
     * 重写shard中获取期次的方法，扫描文件夹拿到输入数据，从具体文件中获取到对应的期次信息
     * @param taskParam
     * @return
     */
    @Override
    public ReturnT<List<String>> executeShard(TaskParam taskParam) {
        log.info("==================shard,根据正则,文件时间获取文件信息===============");
        ReturnT<List<String>> result = new ReturnT<List<String>>();
        List<String> issueList = new ArrayList<String>();
        // 1.解析参数
        ProductParam productParam = JSON.parseObject(taskParam.getModelParameters(), ProductParam.class);
        // 2.根据输入文件信息匹配xml,根据日期替换掉输入文件里的yyyyMMdd 等信息
        List<String> issues = ShardUtil.getIssueList(taskParam.getModelParameters(), taskParam.getFixedMap(), taskParam.getDynamicMap());
        log.info("====输入需要处理的期次有【{}】===",StringUtils.join(issues,","));
        for (String issue:issues){
            this.getIssueList(issueList,productParam, taskParam, issue);
        }
        log.info("=====总共需要处理【{}】期文件===============",issueList.size());
        result.setData(issueList);
        return result;
    }

    /**
     *
     * @param productParam
     * @param dyMap
     * @param s
     * @return
     */
    public void getIssueList(List<String> issueList, ProductParam productParam, TaskParam taskParam, String s){
        LinkedHashMap dyMap = taskParam.getDynamicMap();
        String filePath = productParam.getInputFilePath();
        String inputFilePath = FileNameUtils.dealFilePath(filePath, s);
        log.info("输入文件路径：{}",inputFilePath);
        String regex = productParam.getFileNamePattern();
        String productKey =(String)dyMap.get("productKey");
        String pattern = FileNameUtils.dealFileNamePattern(regex,s);
        log.info("文件正则：{}",pattern);
        //已经入库的数据信息
        List<String> dbIssueList = productInfoService.queryDbIssueList(taskParam.getProductId(),productParam.getCycle(),productParam.getSatellite());
        List<File> fileList = FileUtil.iteratorFileAndDirectory(new File(inputFilePath), pattern);
        String finalPattern = pattern;
        Optional.of(fileList).orElse(new ArrayList<>())
                .forEach(file -> {
                    //根据文件名，正则,读取到对应的期次信息,过滤掉已入库的期次
                    String issue = this.getFileIssueTime(file.getPath(), finalPattern,dbIssueList,productKey);
                    if(StringUtils.isNotEmpty(issue)){
                        //通过 #将文件输入路径与期次拼接
                        issueList.add(file.getPath()+"#"+issue);
                    }
                });
    }

    /**
     * 获取文件路径中的期次信息
     * @param path
     * @param regex
     * @param dbIssueList
     * @return
     */
    private String getFileIssueTime(String path,String regex,List<String> dbIssueList,String productKey) {
        String issue = null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(new File(path).getName());
        if(matcher.find()){
            //issue长度为12，此处可以根据正则匹配任意长度，后续遇到具体使用情况完善
            issue = matcher.group(1).replace("_","");
        }else {
            return null;
        }
        //FY4A数据是世界时 期次加8个小时
        if(productKey.contains(BaseProductServiceConstant.SATELLITE_NAME)){
            issue = this.getBeijingTimeIssue(issue);
        }
        //过滤掉已经入库的信息
        if(dbIssueList.contains(issue)){
            return null;
        }
        return issue;
    }

    /**
     * 世界时转北京时 +8
     * @param issue
     * @return
     */
    private String getBeijingTimeIssue(String issue) {
        Date date = DateUtil.strToDate(issue, DateConstant.YYYYMMDDHHMM);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR,+8);
        Date time = calendar.getTime();
        return DateUtil.dateToStr(time, DateConstant.YYYYMMDDHHMM);
    }

    @Override
    public String getIssueFromInputFile(String issue) {
        //期次从shard中获取,截取#号后面的内容
        return issue.substring(issue.indexOf("#")+1);
    }

}
