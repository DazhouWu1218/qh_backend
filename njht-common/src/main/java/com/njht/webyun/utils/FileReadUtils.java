package com.njht.webyun.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @Author: 代国军
 * @CreateDate: 2022/1/20 15:20
 * @Description: 读取文件内容工具类
 */
@Slf4j
public class FileReadUtils {

    private FileReadUtils() { }

    /**
     *
     * @param logFileName
     * @return
     */
    public static String readLogByPath(String logFileName) {
        // read file
        if (logFileName == null || logFileName.trim().length() == 0) {
            return "readLog fail, logFile not exists";
        }
        File logFile = new File(logFileName);
        if (!logFile.exists()) {
            return "readLog fail, logFile not exists";
        }
        int toLineNum = 0;
        LineNumberReader reader = null;
        StringBuilder logContentBuffer = new StringBuilder();
        try {
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(logFile), StandardCharsets.UTF_8));
            String line = null;
            while ((line = reader.readLine()) != null) {
                toLineNum = reader.getLineNumber();
                if (toLineNum >= 1) {
                    line = line.replace("<progress>", "");
                    line = line.replace("</progress>", "");
                    line = line.replace("<br>----------- htht-job  callback success", "");
                    logContentBuffer.append(line).append("<br>");
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        // result
        return logContentBuffer.toString();
    }
}
