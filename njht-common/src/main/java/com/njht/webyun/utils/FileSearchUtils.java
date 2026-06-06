package com.njht.webyun.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author daiguojun
 */
@Slf4j
public class FileSearchUtils {

    /**
     * 寻找文件夹下匹配的所有文件
     *
     * @param directory
     * @param pattern
     * @return
     */
    public static List<File> getMatchFile(File directory, Pattern pattern) {
        log.info("递归文件夹:[{}],正则:[{}]", directory.getAbsolutePath(), pattern.toString());
        final List<File> matchedFileList = new ArrayList<>();
        findMatchFile(matchedFileList, directory, pattern);
        return matchedFileList;
    }


    private static void findMatchFile(List<File> matchedFileList, File directory, Pattern pattern) {
        final File[] files = Optional.ofNullable(directory.listFiles()).orElseGet(() -> new File[0]);
        for (File file : files) {
            if (pattern.matcher(file.getName()).find()) {
                log.debug("查找到文件:[{}]", file.getAbsolutePath());
                matchedFileList.add(file);
            } else if (file.isDirectory()) {
                findMatchFile(matchedFileList, file, pattern);
            }
        }
    }


    /**
     * 寻找包含正则匹配到的文件的目录
     * @param directory
     * @param pattern
     * @return
     */
    public static File getDataDirectory(File directory, Pattern pattern) {
        final File[] childFiles = Optional.ofNullable(directory.listFiles()).orElseGet(() -> new File[0]);
        for (File childFile : childFiles) {
            final boolean find = pattern.matcher(childFile.getName()).find();
            if (find) {
                return directory;
            } else if (childFile.isDirectory()) {
                final File containMatchedFileDirectory = getDataDirectory(childFile, pattern);
                if (containMatchedFileDirectory != null) {
                    return containMatchedFileDirectory;
                }
            }
        }
        return null;
    }

    /**
     * 获取替换之后的文件路径
     * @param path
     * @return
     */
    public static String getReplaceFilePath(String path){
        String replace = path.replace("\\", "/");
        return replace;
    }

    public static List<File> getSubFiles(File file) {
        return FileZipUtils.getSubFiles(file);
    }

    public static List<File> getFileList(File file,String regex) {
        List<File> subFiles = getSubFiles(file);
        Pattern pattern= Pattern.compile(regex);
        List<File> regexList = new ArrayList<>();
        for(File f:subFiles){
            Matcher m = pattern.matcher(f.getName());
            if(m.find()){
                regexList.add(f);
            }
        }
        return regexList;

    }

    public static List<String> getFileList(String path){
        List<String> list =new ArrayList<>();
        File file = new File(path);
        if(!file.exists()){
            return new ArrayList<>();
        }
        List<File> subFiles = getSubFiles(file);
        String regex = ".db";
        Pattern p = Pattern.compile(regex);
        for(File f:subFiles){
            String filename = f.getName();
            Matcher m = p.matcher(filename);
            if(m.find()){
                continue;
            }
            String path1 = f.getPath().replace("\\","/");
            list.add(path1);
        }
        Collections.reverse(list);
        return list;
    }
}
