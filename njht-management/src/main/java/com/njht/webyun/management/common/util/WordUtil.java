package com.njht.webyun.management.common.util;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dgj
 */
@Component
@Slf4j
public class WordUtil {

    @Value("${algorithm.exePath}")
    private String exePath;

    /**
     * word 转pdf
     * @param list
     * @return
     */
    @Async
    public void wordToPdf(List<String> list){
        Map<String,String> map = new HashMap<>(16);
        for (String s:list){
            String  inPath = s;
            String outPath = inPath.substring(0,inPath.lastIndexOf("."))+".pdf";
            if(new File(outPath).exists()){
                continue;
            }
            executeCmd(inPath);
        }
    }

    @Async
    public void wordToPdf(List<String> list, Map<String,String> map){
        list.stream().parallel().forEach(s -> {
            String inPath = s;
            String outPath = map.get(s);
            File inputFile = new File(inPath);
            if(!inputFile.exists()){
                return;
            }
            FileOutputStream os = null;
            //获取文件
            try {
                long old = System.currentTimeMillis();
                File file = new File(outPath);
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                if(file.exists() && file.length()!= 0){
                    return;
                }else {
                    file.delete();
                }
                //获取文件流
                os = new FileOutputStream(file);
                // Address是将要被转化的word文档
                Document doc = new Document(inPath);
                // 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF
                doc.save(os, SaveFormat.PDF);
                long now = System.currentTimeMillis();
                log.info(inPath+"---"+(now-old)/1000+"s，转换完成");
            } catch (Exception e) {
                log.info(outPath+"文件转换失败");
                e.printStackTrace();
            }finally {
                if(os!=null){
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void executeCmd(String docPath) {
        String cmd = exePath + " " + docPath;
        log.info("-----executeCmd==" + cmd);
        ProcCmd pc = new ProcCmd();
        pc.setCharsetName("utf8");
        pc.setLogFilePath("\\logs\\exelog");
        pc.exec(cmd);
    }
}
