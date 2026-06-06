package com.njht.webyun.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/12/23 10:48
 * @Description: 图片压缩工具类
 */
@Slf4j
public class ImgResizeUtils {

    private ImgResizeUtils() {}

    private static final String THUMB = "thumb";
    private static final String JPG = "jpg";
    private static final String PNG = "png";

    /**
     * 压缩图片
     * @param filePath
     * @return
     */
    public static File getReSizeFile(String filePath) {
        // 源目录
        File targetFile = new File(filePath);
        String s = targetFile.getName().substring(targetFile.getName().lastIndexOf("."));
        if(s.contains(JPG)||s.contains(PNG)){
            // 目标目录
            String toPath = targetFile.getParentFile()+File.separator+THUMB+File.separator+targetFile.getName();
            File toFile = new File(toPath);
            //如果文件已经压缩过 直接返回压缩之后的文件
            if(toFile.exists()){
                return toFile;
            }else {
                if(!toFile.getParentFile().exists()){
                    toFile.getParentFile().mkdirs();
                }
                Resource resource =new PathResource(Paths.get(targetFile.getPath()));
                try {
                    return resizeImage(resource, toPath);
                } catch (IOException e) {
                    log.info("==========图片压缩::未找到文件路径 [{}]===========",targetFile);
                    return targetFile;
                }
            }
        }else {
            return targetFile;
        }

    }


    /**
     * 获取压缩之后的图片路径
     * @param fileList
     * @return
     */
    public static List<File> getReSizeFileList(List<String> fileList) {
        List<File> files = new ArrayList<>();
        for(String path:fileList){
            files.add(getReSizeFile(path));
        }
        return files;
    }

    /**
     * 压缩图片
     * @param resource
     * @param path
     * @return
     * @throws IOException
     */
    public static File resizeImage(final Resource resource,final String path) throws IOException {
        Image image = ImageIO.read(resource.getInputStream());
        int ow = image.getWidth(null);
        int oh = image.getHeight(null);
        int tw = 700;
        int th = 700;

        if (tw <= -1 && th <= -1) {
            tw = ow;
            th = oh;
        }else if (tw > -1) {
            th = tw * oh / ow;
        } else if (th > -1) {
            tw = th * ow / oh;
        }
        // 目标路径
        File dest = new File(path);
        resize(image, dest, tw, th);
        return dest;
    }

    /**
     * 修改图片大小
     * @param src
     * @param dest
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    public static boolean resize(final Image src, final File dest, int width, int height) throws IOException {
        BufferedImage bfi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bfi.getGraphics().drawImage(src, 0, 0, width, height, null);
        bfi.getGraphics().dispose();
        return ImageIO.write(bfi, "PNG", dest);
    }
}
