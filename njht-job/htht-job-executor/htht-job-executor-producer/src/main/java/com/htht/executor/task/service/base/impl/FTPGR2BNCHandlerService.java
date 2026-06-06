package com.htht.executor.task.service.base.impl;

import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.util.FileNameUtils;
import com.htht.job.core.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
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
@Service("FTPGR2BNCHandlerService")
@Slf4j
public class FTPGR2BNCHandlerService extends BaseCheckFileHandlerService{

    /**
     *
     * @param productParam
     * @param dyMap
     * @param s
     * @return
     */
    @Override
    public void getIssueList(List<String> issueList, ProductParam productParam, LinkedHashMap dyMap, String s){
        String filePath = productParam.getInputFilePath();
        String inputFilePath = FileNameUtils.dealFilePath(filePath, s);
        log.info("输入文件路径：{}",inputFilePath);
        String regex = productParam.getFileNamePattern();
        log.info("文件正则：{}",regex);
        List<File> fileList = FileUtil.iteratorFileAndDirectory(new File(inputFilePath), regex);

        // 排序拿到最新一期 匹配到期次并返回
        Optional<File> file = Optional.of(fileList).orElse(new ArrayList<>())
                .stream()
                .sorted(Comparator.comparing(File::getName).reversed()).findFirst();
        if ( file.isPresent() ) {
            File file1 = file.get();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(file1.getName());
            if (matcher.find()) {
                String group = matcher.group(1);
                issueList.add(file1.getPath()+"#"+group);
            }
        }
    }

}
