package com.htht.job.core.util;

import com.htht.job.core.date.DataTimeHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件名工具类
 *
 * @author zhouhouliang
 */
public class FileNameUtils {

    /**
     * 获取压缩包的去除文件类型的文件名
     * 支持去除 .zip .rar .tar .tar.gz
     * 普通文件仅去除后缀
     *
     * @param file
     * @return
     */
    public static String getFileRealName(File file) {
        final String fileName = file.getName();
        if (file.isDirectory()) {
            return fileName;
        } else {
            final Pattern zipPattern = Pattern.compile("(^.*)(\\.zip$|\\.rar$|(?<!\\.tar)\\.gz$|\\.tar\\.gz$)");
            final Matcher matcher = zipPattern.matcher(fileName);
            if (matcher.find()) {
                return matcher.group(1);
            } else {
                if (fileName.contains(".")) {
                    final int lastIndexOf = fileName.lastIndexOf(".");
                    return fileName.substring(0, lastIndexOf);
                } else {
                    return fileName;
                }
            }
        }
    }

    /**
     * 获取文件的后缀名
     * a.png
     *
     *
     * @param file
     * @return
     */
    public static String getFileSuffix(File file) {
        final String fileName = file.getName();
        if (fileName.contains(".")) {
            final int lastIndexOf = fileName.lastIndexOf(".");
            return fileName.substring(lastIndexOf+1);
        } else {
            return "";
        }
    }


    /**
     * url 中的反斜换成 正斜
     *
     * @param url
     * @return
     */
    public static String formatURIasLinuxStyle(String url) {
        Assert.isTrue(StringUtils.isNotBlank(url), "url 不能为空");
        return url.replace("\\", "/");
    }


    /**
     * 获取不带后缀名的文件名
     *
     * @param file
     * @return
     */
    public static String getFileNameWithoutSuffix(File file) {
        String file_name = file.getName();
        if (file_name.endsWith(".tar.gz")) {
            return file_name.replace(".tar.gz", "");
        } else {
            return file_name.substring(0, file_name.lastIndexOf("."));
        }
    }

    /**
     * 获取文件总大小
     *
     * @param file
     * @return
     */
    public static Long getDirSize(File file) {
        // 判断文件是否存在
        if (file.exists()) {
            // 如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                Long size = 0L;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {// 如果是文件则直接返回其大小,以“兆”为单位
                Long size = file.length();
                return size;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0L;
        }
    }

    /**
     * 根据模式替换文件名称
     * @param pattern
     * @param issue
     * @return
     */
    public static String dealFileNamePattern(String pattern,String issue) {
        return dealFilePath(pattern,issue);
    }

    public static String dealFilePath(String filePath,String issue) {
        filePath = filePath.replace("{", "").replace("}", "");
        String yyyy = issue.substring(0, 4);
        String MM = issue.substring(4, 6);
        String dd = issue.substring(6, 8);
        if(filePath.contains("dd-1")){
            yyyy = String.valueOf(Integer.parseInt(dd)-1);
        }
        if(issue.length()>=9 && issue.length()<=12){
            String HHmm = issue.substring(8,12);
            filePath = filePath.replace("HHmm", HHmm);
        }
        filePath = filePath.replace("dd-1", dd);
        filePath = filePath.replace("yyyyMMdd", yyyy+MM+dd);
        filePath = filePath.replace("yyyyMM", yyyy+MM);
        filePath = filePath.replace("yyyy", yyyy);
        filePath = filePath.replace("MM", MM);
        filePath = filePath.replace("dd", dd);
        // issue批次直接替换路径
        filePath = filePath.replace("issue",issue);
        return filePath;
    }

    public static String dealFileNamePatternLastYear(String pattern,String issue) {
        return dealFilePathLastYear(pattern,issue);
    }


    public static String dealFilePathLastYear(String filePath,String issue) {
        filePath = filePath.replace("{", "").replace("}", "");
        String yyyy = String.valueOf(Integer.parseInt(issue.substring(0, 4))-1);
        String dd = issue.substring(6, 8);
        String MM = issue.substring(4, 6);
        filePath = filePath.replace("yyyyMMdd", yyyy+MM+dd);
        filePath = filePath.replace("yyyyMM", yyyy+MM);
        filePath = filePath.replace("yyyy", yyyy);
        filePath = filePath.replace("MM", MM);
        filePath = filePath.replace("dd", dd);
        return filePath;
    }

    /**
     * 根据时间规则过滤（选择白日影像）
     * @param file
     * @param timeRegex
     * @return
     */
    public static boolean filterFilePathByTimeRegex(File file, String timeRegex){
        // 文件名中的时间
        Date fileDate = new Date(DataTimeHelper.getDataTimeFromFileNameByPattern(file.getName(),timeRegex));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fileDate);
        if (calendar.get(Calendar.HOUR)>=5 && calendar.get(Calendar.HOUR)<=19) {
            return true;
        }
        return false;

    }
}
