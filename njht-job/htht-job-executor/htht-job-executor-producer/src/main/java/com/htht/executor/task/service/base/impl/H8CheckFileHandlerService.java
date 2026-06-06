package com.htht.executor.task.service.base.impl;

import com.alibaba.fastjson.JSON;
import com.htht.executor.product.service.ProductInfoService;
import com.htht.executor.task.util.ShardUtil;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.constant.DateConstant;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.redis.RedisService;
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
@Service("h8CheckFileHandlerService")
@Slf4j
public class H8CheckFileHandlerService  extends BaseCheckFileHandlerService {


    public static final String NOW = "now";
    public static final String HISTORY = "history";

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private RedisService redisService;


    /**
     * 重写shard中获取期次的方法，扫描文件夹拿到输入数据，从具体文件中获取到对应的期次信息
     * @param taskParam
     * @return
     */
    @Override
    public ReturnT<List<String>> executeShard(TaskParam taskParam) {
        log.info("==================H8Shard========================");
        ReturnT<List<String>> result = new ReturnT<>();
        List<String> issueList = new ArrayList<String>();
        // 1.解析参数
        ProductParam productParam = JSON.parseObject(taskParam.getModelParameters(), ProductParam.class);
        String cycle = productParam.getCycle();
        //已经入库的数据信息
        List<String> dbIssueList = productInfoService.queryDbIssueList(taskParam.getProductId(), cycle, productParam.getSatellite());
        String dateType = productParam.getDateType();
        if(NOW.equals(dateType)){
            this.getIssueList(productParam,dbIssueList,issueList,null);
        }
        if(HISTORY.equals(dateType)){
            List<String> dayList = ShardUtil.getIssueList(taskParam.getModelParameters(), taskParam.getFixedMap(), taskParam.getDynamicMap());
            for(String issueStr :dayList){
                this.getIssueList(productParam,dbIssueList,issueList,issueStr);
            }
        }
        result.setData(issueList);
        log.info("============总共需要处理[{}]期数据",issueList.size());
        return result;
    }

    private void getIssueList(ProductParam productParam, List<String> dbIssueList, List<String> issueList, String issueStr) {
        List<String> list = Optional.ofNullable(dbIssueList).orElse(new ArrayList<>());
        String inputFilePath = productParam.getInputFilePath();
        inputFilePath = this.getInputFilePath(inputFilePath,issueStr);
        String pattern = productParam.getFileNamePattern();
        List<File> fileList = FileUtil.iteratorFileAndDirectory(new File(inputFilePath), pattern);
        Optional.ofNullable(fileList).orElse(new ArrayList<>())
                .forEach(file -> {
                    //根据文件名，正则,读取到对应的期次信息
                    String issue = getFileIssueTime(file.getName(), pattern);
                    if(StringUtils.isNotEmpty(issue) && !list.contains(issue)){
                        //通过 #将文件输入路径与期次拼接
                        issueList.add(file.getPath()+"#"+issue);
                    }
                });
    }


    /**
     * 获取文件路径中的期次信息
     * @param path
     * @param regex
     * @return
     */
    private String getFileIssueTime(String path,String regex) {
        String issue = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);
        if(matcher.find()){
            issue = matcher.group(1);
        }else{
            return null;
        }
        return issue.replace("_","");
    }

    @Override
    public String getIssueFromInputFile(String issue) {
        //期次从shard中获取,截取#号后面的内容
        return issue.substring(issue.indexOf("#")+1);
    }

    private static String getInputFilePath(String inputFilePath,String issue){
        if(StringUtils.isBlank(issue)){
            Calendar calendar = Calendar.getInstance();
            Date time = calendar.getTime();
            calendar.add(Calendar.HOUR,-8);
            issue = DateUtil.formatDateTime(time, DateConstant.YYYYMMDDHHMMSS);
        }
        return FileNameUtils.dealFilePath(inputFilePath,issue);
    }


    public static void main(String[] args) {
        String input = "{yyyyMMdd}";
        String inputFilePath = getInputFilePath(input, null);
        System.out.println(inputFilePath);
    }
}
