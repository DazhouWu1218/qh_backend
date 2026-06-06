package com.njht.webyun.publish.product.util;

import com.njht.webyun.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author dgj
 */
@Component
@Slf4j
public class WordUtil {

    /**
     * word 转pdf
     * @return
     */
    public static void wordToPdf(String filePath, String exePath) {
        new Thread(() -> {
            String outPath = filePath.substring(0, filePath.lastIndexOf("."))+".pdf";
            if(new File(outPath).exists()){
                return;
            }
            log.info("start wordToPdf,now thread name:{}",Thread.currentThread().getName());
            try {
                executeCmd(exePath, filePath);
            } catch (Exception e) {
                throw new CommonException(e.getMessage());
            }
        }).start();
    }

    private static void executeCmd(String exePath, String filePath) {
        String cmd = exePath + " " + filePath;
        log.info("-----executeCmd==" + cmd);
        ProcCmd pc = new ProcCmd();
        pc.setCharsetName("utf8");
        pc.setLogFilePath("\\logs\\exelog");
        pc.exec(cmd);
    }

}
