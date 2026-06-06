package com.njht.webyun.management.common.util;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author dgj
 */
public class FileUnpackUtil {
    /**
     * 解压缩tar以及tar.gz文件
     *
     * @param file tar以及tar.gz文件地址
     */
    public static void unpackTarGz(File file) {
        unpackTarGz(file, file.getParentFile());
    }

    /**
     * 解压缩tar以及tar.gz文件
     *
     * @param srcFile  tar以及tar.gz文件地址
     * @param destFile 目标文件保存地址
     */
    public static void unpackTarGz(File srcFile, File destFile) {
        TarInputStream tarIn = null;
        OutputStream out = null;
        try {
            tarIn = new TarInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(srcFile))), 1024 * 2);
            createDirectory(destFile, null);
            //创建输出目录
            TarEntry entry = null;
            while ((entry = tarIn.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    //是目录
                    entry.getName();
                    createDirectory(destFile, entry.getName());
                    //创建空目录
                } else {//是文件
                    File tmpFile = new File(destFile, entry.getName());
                    //创建输出目录
                    createDirectory(tmpFile.getParentFile(), null);

                    out = new FileOutputStream(tmpFile);
                    int length = 0;
                    byte[] b = new byte[2048];
                    while ((length = tarIn.read(b)) != -1) {
                        out.write(b, 0, length);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (tarIn != null) {
                    tarIn.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解压缩zip文件
     *
     * @param file    zip文件地址
     * @param charset 字符编码 window系统中文适用GB2312能解决乱码
     */
    public static void unpackZip(File file, String charset) throws IOException {
        unpackZip(file, file.getParentFile(), charset);
    }

    public static void unpackZip(File file,File destFile) throws IOException {
        unpackZip(file,destFile,"GBK");
    }

    /**
     * 解压缩zip文件
     * @param zipFile  zip文件地址
     * @param pathFile 解压缩后存放地址
     * @param charset  字符编码 window系统中文适用GB2312能解决乱码
     */
    public static void unpackZip(File zipFile, File pathFile, String charset) throws IOException {
        InputStream in =null;
        OutputStream out =null;
        ZipFile zip  = null;
        try{
            if(!zipFile.exists()){
                throw new IOException("需解压文件不存在.");
            }
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            zip = new ZipFile(zipFile, Charset.forName(charset));
            for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                in = zip.getInputStream(entry);
                String outPath = (pathFile.getPath() + File.separator + zipEntryName).replaceAll("\\*", "/");
                // 判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                // 输出文件路径信息
                out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }
        }catch(Exception e){
            throw new IOException(e);
        }finally {
            if(zip !=null){
                zip.close();
            }
            if (in!=null){
                in.close();
            }
            if (out!=null){
                out.close();
            }
        }
    }

    private static void createDirectory(File file) {
        createDirectory(file, null);
    }

    private static void createDirectory(File file, String subDir) {
        if (!(subDir == null || "".equals(subDir.trim()))) {
            //子目录不为空
            file = new File(file, subDir);
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.mkdirs();
        }
    }

    public static boolean copy(String from, String to) {
        to = replace(to, " \\", "/ <file://\\> ");
        String toPath = to.substring(0, to.lastIndexOf("/"));
        File f = new File(toPath);
        if (!f.exists()){
            f.mkdirs();
        }
        try (
                BufferedInputStream bin = new BufferedInputStream(new FileInputStream(from));
                BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(to));
        ){
            int c;
            while ((c = bin.read()) != -1){
                bout.write(c);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    static public String replace(String srcStr, String oldStr, String newStr) {
        int i = srcStr.indexOf(oldStr);
        StringBuffer sb = new StringBuffer();
        if (i == -1) {
            return srcStr;
        }
        sb.append(srcStr.substring(0, i) + newStr);
        if (i + oldStr.length() < srcStr.length()) {
            sb.append(replace(
                    srcStr.substring(i + oldStr.length(), srcStr.length()),
                    oldStr, newStr));
        }
        return sb.toString();
    }

}
