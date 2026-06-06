package com.htht.job.core.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 系统命令调用工具类
 *
 * @author zhouhouliang
 */
@Slf4j
public class SystemCommandUtil {

    private SystemCommandUtil () {

    }

    @SneakyThrows
    public static String execute(String cmd) {
        final long start = System.currentTimeMillis();
        log.info("开始执行CMD:[{}]", cmd);
        final Process process = Runtime.getRuntime().exec(cmd);
        final StringBuilder cmdOutputSB = new StringBuilder();
        String infoLine;
        String errorLine = null;
        try (
                // inputStream
                final InputStream inputStream = process.getInputStream();
                final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                //  errorStream
                final InputStream errorStream = process.getErrorStream();
                final InputStreamReader errorInputStreamReader = new InputStreamReader(errorStream);
                final BufferedReader errorBufferedReader = new BufferedReader(errorInputStreamReader);
        ) {
            while ((infoLine = bufferedReader.readLine()) != null || (errorLine = errorBufferedReader.readLine()) != null) {
                //输出exe输出的信息以及错误信息
                if (infoLine != null) {
                    log.info(infoLine);
                    cmdOutputSB.append(infoLine + "\n");
                } else {
                    log.error(errorLine);
//                    cmdOutputSB.append(errorLine + "\n");
                }
            }
            log.info("结束执行CMD:[{}],耗时:{}ms", cmd, System.currentTimeMillis() - start);
        } catch (Exception e) {
            throw e;
        }
        return cmdOutputSB.toString();
    }

    public static void execute(String exePath, String inputXmlPath) {
        execute(exePath+" "+inputXmlPath);
    }
}
