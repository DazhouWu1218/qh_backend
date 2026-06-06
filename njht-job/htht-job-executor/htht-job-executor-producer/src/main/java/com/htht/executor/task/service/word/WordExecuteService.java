package com.htht.executor.task.service.word;

import com.htht.job.core.util.ScriptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

@Service("wordExecuteService")
@Slf4j
public class WordExecuteService {


    /**
     * 异步执行word 转换
     * @param filePath
     * @param exePath
     */
    @Async("taskExecutor")
    public void wordToPdf(String filePath, String exePath){
        log.info("start wordToPdf,now thread name:{}",Thread.currentThread().getName());
        String outPath = filePath.substring(0, filePath.lastIndexOf("."))+".pdf";
        if(new File(outPath).exists()){
            return;
        }
        try {
            // do trigger
            ScriptUtil.execute(exePath, filePath,"10");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
