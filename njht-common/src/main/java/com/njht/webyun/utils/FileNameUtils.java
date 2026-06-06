package com.njht.webyun.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.File;
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

}
