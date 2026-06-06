package com.njht.webyun.management.dataanalysis.systemdatacount.utils;


import lombok.SneakyThrows;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author zhouhouliang
 */
public class FileOperationUtil {


    /**
     * 获取压缩包的去除文件类型的文件名
     * 支持去除 .zip .rar .tar .tar.gz
     * 不支持的文件名原样返回
     *
     * @param fileName
     * @return
     */
    public static String getFileRealName(String fileName) {
        final Pattern zipPattern = Pattern.compile("(^.*)(\\.zip$|\\.rar$|(?<!\\.tar)\\.gz$|\\.tar\\.gz$)");
        final Matcher matcher = zipPattern.matcher(fileName);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return fileName;
        }
    }

    /**
     * 获取文件或者文件夹大小
     *
     * @param file
     * @return
     */
    public static Long getFileSize(File file) {
        Assert.isTrue(file.exists(), "文件[" + file.getAbsolutePath() + "]不存在");
        if (file.isFile()) {
            return FileUtils.sizeOf(file);
        } else {
            return FileUtils.sizeOfDirectory(file);
        }
    }


    /**
     * 统计文件/文件夹 下的数据量  ,按日分组
     *
     * @param file
     * @return
     */
    @SneakyThrows
    public static Map<String, Long> getFileSizeGroupByDay(File file, Date validDate) {
        Assert.isTrue(file.exists(), "文件[" + file.getAbsolutePath() + "]不存在");
        final Map<String, Long> dateSizeMap = new HashMap<>();
        if (file.isFile()) {
            dateSizeMap.put(getFileCreateTime(file), file.length());
            return dateSizeMap;
        } else {
            // 统计文件夹下的文件大小,按日期分组统计
            countDirectoryDateSize(file, dateSizeMap, validDate);
        }
        return dateSizeMap;
    }

    /**
     * 统计文件夹下的文件大小,按日期分组统计
     *
     * @param file
     * @param dateSizeMap
     */
    private static void countDirectoryDateSize(File file, Map<String, Long> dateSizeMap, Date validDate) {
        // 日期过滤
        if (!checkFileTimeValid(file, validDate)) {
            return;
        }
        if (file.isDirectory()) {
            // 是文件夹
            final File[] childFiles = file.listFiles();
            if (ArrayUtils.isNotEmpty(childFiles)) {
                for (File childFile : childFiles) {
                    countDirectoryDateSize(childFile, dateSizeMap, validDate);
                }
            }
        } else {
            // 是文件
            final long length = file.length();
            final String date = getFileCreateTime(file);
            final Long count = Optional.ofNullable(dateSizeMap.get(date)).orElse(0L);
            dateSizeMap.put(date, count + length);
        }
    }

    /**
     * 获取文件创建时间 yyyyMMdd
     *
     * @return
     */
    @SneakyThrows
    private static String getFileCreateTime(File file) {
        final BasicFileAttributes basicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        final FileTime fileTime = basicFileAttributes.creationTime();
        return DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.systemDefault()).format(fileTime.toInstant());
    }


    /**
     * 检查文件修改/创建时间是否在有效时间之后
     *
     * @param file
     * @param validDate
     * @return
     */
    @SneakyThrows
    public static boolean checkFileTimeValid(File file, Date validDate) {
        // 有效时间
        Calendar validCalendar = Calendar.getInstance();
        validCalendar.setTime(validDate);
        // 获得文件创建时间
        Calendar calendar = Calendar.getInstance();
        if (file.isDirectory()) {
            // 如果是文件夹,那么创建时间和修改时间都不可信,有可能修改与创建很久,但是子孙级有最新的文件,直接判定有效
            return true;
        } else {
            FileTime fileTime = Files.readAttributes(Paths.get(file.getAbsolutePath()), BasicFileAttributes.class).creationTime();
            calendar.setTimeInMillis(fileTime.toMillis());
            return !calendar.before(validCalendar);
        }

    }


    /**
     * 获取文件路径的相对路径,就是讲路径前缀去掉
     *
     * @param pathPrefix
     * @param absolutePath
     * @return
     */
    public static String getRelativePath(String pathPrefix, String absolutePath) {
        Assert.isTrue(StringUtils.isNotBlank(pathPrefix), "pathPrefix 不能为空");
        Assert.isTrue(StringUtils.isNotBlank(absolutePath), "absolutePath 不能为空");
        Assert.isTrue(absolutePath.contains(absolutePath), "absolutePath不包含pathPrefix");
        String relativePath = absolutePath.substring(pathPrefix.length());
        if (relativePath.startsWith("/") || relativePath.startsWith("\\")) {
            relativePath = relativePath.substring(1);
        }
        return relativePath;
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
     * 向下寻找真正的数据文件夹
     *
     * @param directory
     * @return
     */
    public static File findDeepestDataDirectory(File directory, String dataXmlRegex) {
        Assert.isTrue(StringUtils.isNotBlank(dataXmlRegex), "数据识别正则不能为空");
        Assert.isTrue(directory.exists(), "文件[" + directory.getAbsolutePath() + "]不存在");
        Assert.isTrue(directory.isDirectory(), "文件[" + directory.getAbsolutePath() + "]不是文件夹");
        final File[] child = Optional.ofNullable(directory.listFiles()).orElseGet(() -> new File[0]);
        final List<File> childXmlFileList = Arrays.stream(child)
                .filter(File::isFile)
                .filter(file -> file.getName().toLowerCase().contains(".xml"))
                .filter(file -> Pattern.compile(dataXmlRegex).matcher(file.getName()).find())
                .collect(Collectors.toList());
        final List<File> childDirectoryList = Arrays.stream(child)
                .filter(File::isDirectory)
                .collect(Collectors.toList());
        if (childXmlFileList.size() > 0) {
            return directory;
        } else if (childDirectoryList.size() > 0) {
            for (File file : childDirectoryList) {
                final File deepestDataDirectory = findDeepestDataDirectory(file, dataXmlRegex);
                if (deepestDataDirectory != null) {
                    return deepestDataDirectory;
                }
            }
        }
        return null;
    }

    /**
     * 删除文件夹及其内容
     *
     * @param file
     */
    @SneakyThrows
    public static void deleteFileOrDirectory(File file) {
        if (file.exists()) {
            final File[] childFiles = Optional.ofNullable(file.listFiles()).orElseGet(() -> new File[0]);
            for (File childFile : childFiles) {
                if (childFile.isFile()) {
                    Files.delete(childFile.toPath());
                } else {
                    deleteFileOrDirectory(childFile);
                }
            }
            Files.delete(file.toPath());
        }
    }


    /**
     * 复制文件或者文件夹
     *
     * @param sourceFile
     * @param upBoxing        (去皮)sourceFile是否保留最外层目录
     * @param targetDirectory
     */
    @SneakyThrows
    public static String copyFileToDirectory(File sourceFile, boolean upBoxing, File targetDirectory) {
        Assert.isTrue(sourceFile.exists(), "源文件不存在");
        if (!targetDirectory.exists()) {
            final boolean mkdirs = targetDirectory.mkdirs();
            Assert.isTrue(mkdirs, "创建目标目录失败");
        }
        return copyFileOrDirectory(sourceFile, upBoxing, targetDirectory);
    }


    @SneakyThrows
    private static String copyFileOrDirectory(File sourceFile, boolean upBoxing, File targetDirectory) {
        if (sourceFile.isFile()) {
            FileUtils.copyFileToDirectory(sourceFile, targetDirectory);
        } else {
            if (upBoxing) {
                // 去皮
                final File[] childFiles = Optional.ofNullable(sourceFile.listFiles()).orElseGet(() -> new File[0]);
                for (File childFile : childFiles) {
                    copyFileOrDirectory(childFile, false, targetDirectory);
                }
            } else {
                final String directoryName = sourceFile.getName();
                final String directoryPath = targetDirectory.getAbsolutePath() + "/" + directoryName;
                final File newDirectory = new File(directoryPath);
                final boolean mkdir = newDirectory.mkdir();
                Assert.isTrue(mkdir, "创建文件夹" + directoryPath + "失败");
                final File[] childFiles = Optional.ofNullable(sourceFile.listFiles()).orElseGet(() -> new File[0]);
                for (File childFile : childFiles) {
                    copyFileOrDirectory(childFile, false, newDirectory);
                }
            }
        }
        return targetDirectory.getAbsolutePath() + "/" + (upBoxing ? "" : sourceFile.getName());
    }

    /**
     * 将文件夹压缩为 tar.gz
     *
     * @param sourceDirectoryFile
     * @param holderDirectory
     */
    public static String compressTarGz(File sourceDirectoryFile, File holderDirectory) {
        Assert.isTrue(sourceDirectoryFile.exists(), "文件夹[" + sourceDirectoryFile.getAbsolutePath() + "]不存在");
        Assert.isTrue(sourceDirectoryFile.isDirectory(), "[" + sourceDirectoryFile.getAbsolutePath() + "]不是文件夹");
        if (!holderDirectory.exists()) {
            final boolean mkdirs = holderDirectory.mkdirs();
            Assert.isTrue(mkdirs, "创建文件夹[" + holderDirectory.getAbsolutePath() + "]失败");
        }

        final Path sourceDirectoryPath = Paths.get(sourceDirectoryFile.getAbsolutePath());

        // 压缩后的文件名
        final String compressFileName = sourceDirectoryFile.getName() + ".tar.gz";
        final String compressFileAbsolutePath = holderDirectory.getAbsolutePath() + "/" + compressFileName;

        //OutputStream输出流、BufferedOutputStream缓冲输出流
        //GzipCompressorOutputStream是gzip压缩输出流
        //TarArchiveOutputStream打tar包输出流（包含gzip压缩输出流）
        try (OutputStream fOut = Files.newOutputStream(Paths.get(compressFileAbsolutePath));
             BufferedOutputStream buffOut = new BufferedOutputStream(fOut);
             GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(buffOut);
             TarArchiveOutputStream tOut = new TarArchiveOutputStream(gzOut)) {
            //遍历文件目录树
            Files.walkFileTree(sourceDirectoryPath, new SimpleFileVisitor<Path>() {

                //当成功访问到一个文件
                @Override
                public FileVisitResult visitFile(Path path,
                                                 BasicFileAttributes attributes) throws IOException {

                    // 判断当前遍历文件是不是符号链接(快捷方式)，不做打包压缩处理
                    if (attributes.isSymbolicLink()) {
                        return FileVisitResult.CONTINUE;
                    }

                    //获取当前遍历文件名称
                    Path targetPath = sourceDirectoryPath.relativize(path);

                    //将该文件打包压缩
                    TarArchiveEntry tarEntry = new TarArchiveEntry(
                            path.toFile(), targetPath.toString());
                    tOut.putArchiveEntry(tarEntry);
                    Files.copy(path, tOut);
                    tOut.closeArchiveEntry();
                    //继续下一个遍历文件处理
                    return FileVisitResult.CONTINUE;
                }

                //当前遍历文件访问失败
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    System.err.printf("无法对该文件压缩打包为tar.gz : %s%n%s%n", file, exc);
                    return FileVisitResult.CONTINUE;
                }

            });
            //for循环完成之后，finish-tar包输出流
            tOut.finish();
            return compressFileAbsolutePath;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
