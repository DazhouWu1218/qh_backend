package com.htht.job.core.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * file tool
 *
 * @author piesat 2017-12-29 17:56:48
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);


    /**
     * delete recursively
     *
     * @param root
     * @return
     */
    public static boolean deleteRecursively(File root) {
        if (root != null && root.exists()) {
            if (root.isDirectory()) {
                File[] children = root.listFiles();
                if (children != null) {
                    for (File child : children) {
                        deleteRecursively(child);
                    }
                }
            }
            return root.delete();
        }
        return false;
    }


    public static void deleteFile(String fileName) {
        // file
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public static boolean deleteFile(File file) {
        try {
            return Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            logger.error(e.toString());
        }
        return false;
    }


    public static void writeFileContent(File file, byte[] data) {

        // file
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        // append file content
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

    }

    public static byte[] readFileContent(File file) {
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(filecontent);
            in.close();

            return filecontent;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 检测文件路径是否存在
     * @param file
     * @return
     */
    public static boolean checkFile(String file){
        if(StringUtils.isBlank(file)){
            return false;
        }
        File f = new File(file);
        return f.isFile()&&f.exists();
    }

    /**
     * 层级遍历rootFile目录下的所有符合标准的文件
     * @param rootFile
     * @param fileRegex
     * @return
     */
    public static List<File> iteratorFileAndDirectory(File rootFile, String fileRegex) {

        if (!rootFile.exists() || !rootFile.isDirectory()) {
            return new ArrayList<>();
        }

        RegexFileFilter regexFileFilter  = new RegexFileFilter(fileRegex);
        List<File> listFiles = (List<File>) FileUtils.listFiles(rootFile, regexFileFilter, DirectoryFileFilter.INSTANCE);
        return listFiles;
    }

    /**
     * 获取文件
     * @param filePath
     * @param fileRegex
     * @return
     */
    public static List<File> iteratorFileAndDirectory(String filePath, String fileRegex) {
        return iteratorFileAndDirectory(new File(filePath),fileRegex);
    }

    /**
     * @param from java.lang.String
     * @param to   java.lang.String
     */
    public static boolean copy(String from, String to) {
        to = replace(to, " \\", "/ <file://\\> ");
        String toPath = to.substring(0, to.lastIndexOf('.'));
        File f = new File(toPath);
        if (!f.exists())
            //创建目标路径的父级目录
            f.getParentFile().mkdirs();
        try(
                BufferedInputStream bin = new BufferedInputStream(new FileInputStream(from));
                //FileOutputStream可以创建文件，但是创建不了多级目录下的文件
                BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(to));
        ){
            int c;
            while ((c = bin.read()) != -1)
                bout.write(c);
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }

    }

    /**
     * @param：ss java.lang.String
     * @return java.lang.String
     */
    public static String replace(String srcStr, String oldStr, String newStr) {
        int i = srcStr.indexOf(oldStr);
        StringBuffer sb = new StringBuffer();
        if (i == -1)
            return srcStr;
        sb.append(srcStr.substring(0, i) + newStr);
        if (i + oldStr.length() < srcStr.length())
            sb.append(replace(srcStr.substring(i + oldStr.length(), srcStr.length()), oldStr, newStr));
        return sb.toString();
    }

    /**
     * 根据正则匹配文件名中的时间
     * @param regex
     * @param name
     * @return
     */
    public static String getFileTime(String regex, String name) {
        String group = null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        if(matcher.find()){
            int i = matcher.groupCount();
            if(i==0){
                group = matcher.group();
            }else{
                StringBuffer stringBuffer = new StringBuffer();
                for (int j=1;j<=i;j++){
                    stringBuffer.append(matcher.group(j));
                }
                group = stringBuffer.toString();
            }
        }
        //规定返回长度为14 不够拼0,不是时间格式的返回null 抛出异常
        if(group != null){
            if(group.length() ==14){
                return group;
            }else if (group.length() == 12){
                return group + "00";
            }else if (group.length() == 10){
                return group + "0000";
            }else if (group.length() == 8){
                return group + "000000";
            }else {
                return null;
            }
        }else{
            return null;
        }
    }


    /**
     * 根据日期格式获取文件名称
     * @param filePath
     * @param issue
     * @return
     */
    public static String getFilePath(String filePath,String issue){
        filePath = filePath.replace("{", "").replace("}", "").replace("issue",issue);
        String yyyy = issue.substring(0, 4);
        String MM = issue.substring(4, 6);
        if(issue.length() == 7){
            issue = issue +"0";
        }
        String dd = issue.substring(6, 8);
        filePath = filePath.replace("yyyyMMdd-1", String.valueOf(Integer.parseInt(yyyy+MM+dd)-1));
        filePath = filePath.replace("yyyyMMdd", yyyy+MM+dd);
        filePath = filePath.replace("yyyyMM", yyyy+MM);
        filePath = filePath.replace("yyyy", yyyy);
        filePath = filePath.replace("MM", MM);
        filePath = filePath.replace("dd", dd);
        return filePath;
    }


    /*public static void appendFileLine(String fileName, String content) {

        // file
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return;
            }
        }

        // content
        if (content == null) {
            content = "";
        }
        content += "\r\n";

        // append file content
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            fos.write(content.getBytes("utf-8"));
            fos.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

    }

    public static List<String> loadFileLines(String fileName){

        List<String> result = new ArrayList<>();

        // valid log file
        File file = new File(fileName);
        if (!file.exists()) {
            return result;
        }

        // read file
        StringBuffer logContentBuffer = new StringBuffer();
        int toLineNum = 0;
        LineNumberReader reader = null;
        try {
            //reader = new LineNumberReader(new FileReader(logFile));
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String line = null;
            while ((line = reader.readLine())!=null) {
                if (line!=null && line.trim().length()>0) {
                    result.add(line);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        return result;
    }*/

}
