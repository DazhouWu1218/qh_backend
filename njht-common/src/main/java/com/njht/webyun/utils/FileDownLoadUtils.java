package com.njht.webyun.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Paths;


/**
 * @Author: 代国军
 * @CreateDate: 2021/11/15 13:44
 * @Description: 文件下载工具类
 */
@Slf4j
public class FileDownLoadUtils {

    private static final String FILENAME = "filename=\"";

    private static final String CHARACTOR_UTF_8 = "UTF-8";

    @SneakyThrows
    public static ResponseEntity downloadFileByPath(String path) {
        try {
            //获取目录
            Resource resource = new UrlResource(Paths.get(path).toUri());
            log.info(resource.getFilename());
            if (resource.exists() && resource.isReadable()){
                return ResponseEntity.ok()
                        .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, FILENAME + URLEncoder.encode(new File(path).getName(), CHARACTOR_UTF_8) + "\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return ResponseEntity.notFound().build();
    }

    public static String downloadFile(String path, HttpServletResponse response) {
        File file = new File(path);
        if (file.exists()) {
            response.setContentType("application/octet-stream");
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + file.getName());
            response.addHeader("fileName", file.getName());
            response.addHeader("Content-Length", "" + file.length());
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                log.info("下载文件成功");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
