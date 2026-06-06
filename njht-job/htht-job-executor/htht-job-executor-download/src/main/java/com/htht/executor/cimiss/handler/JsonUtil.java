package com.htht.executor.cimiss.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;

/**
 * @author daiguojun
 */
@Slf4j
public class JsonUtil {

    public static boolean createJSONFile(String jsonString, String filePath,String fileName){
        boolean flag = true;

        if(jsonString == null){
            return false;
        }

        try {
            // 拼接文件完整路径// 生成json格式文件
            String fullPath = filePath + File.separator + fileName + ".json";
            // 保证创建一个新文件
            File file = new File(fullPath);
            // 如果父目录不存在，创建父目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // 如果已存在,删除旧文件
            if (file.exists()) {
                log.info("————————该区域json文件以存在——————————");
                return true;
            }
            file.createNewFile();//创建新文件

            if(jsonString.indexOf("'")!=-1){
                //将单引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
                jsonString = jsonString.replaceAll("'", "\\'");
            }
            if(jsonString.indexOf("\"")!=-1){
                //将双引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
                jsonString = jsonString.replaceAll("\"", "\\\"");
            }

            if(jsonString.indexOf("\r\n")!=-1){
                //将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
                jsonString = jsonString.replaceAll("\r\n", "\\u000d\\u000a");
            }
            if(jsonString.indexOf("\n")!=-1){
                //将换行转换一下，因为JSON串中字符串不能出现显式的换行
                jsonString = jsonString.replaceAll("\n", "\\u000a");
            }
            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean createJSONFile(Map<String,Object> map,String filePath, String fileName){
        String s = JSONObject.toJSONString(map);
        return createJSONFile(s,filePath,fileName);
    }

    public static String readJsonFile(File file) {
        BufferedReader bReader = null;
        String strall = "";
        try {
            bReader = new BufferedReader(new FileReader(file));
            // 一行一行的写
            String strLine = null;
            while ((strLine = bReader.readLine()) != null) {
                strall = strall + strLine;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bReader != null) {
                try {
                    bReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return strall;
    }

    public static String readJsonFile(String path){
        File file = new File(path);
        return readJsonFile(file);
    }
}
