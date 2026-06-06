package com.njht.webyun.utils;

import com.njht.webyun.enums.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/26 14:00
 * @Description: 文件上传工具类
 */
@Slf4j
public class FileUploadUtils {


    /**
     * 上传多个文件并返回文件路径,文件大小或者路径有问题返回null
     * @param files
     * @return
     */
    public static  ReturnT<List<String>> uploadImgFiles(List<MultipartFile> files,String upLoadPath,String userId) {
        List<String> list = new ArrayList<>();
        for(MultipartFile file:files){
            ReturnT<String> r = uploadImgFile(file, upLoadPath,userId);
            if(ReturnT.SUCCESS_CODE != r.getCode()){
                return ReturnT.failed(r.getCode(),r.getMsg());
            }
            list.add(r.getData());
        }
        return ReturnT.success(list);
    }

    /**
     * 某个用户 上传单个文件
     * @param file
     * @param upLoadPath  上传图片路径
     * @return
     */
    public static ReturnT<String> uploadImgFile(MultipartFile file,String upLoadPath,String userId) {
        //获取原始名字
        String filename = file.getOriginalFilename();
        //正则匹配文件格式
        String regex = "bmp$|jpeg$|jpg$|gif$|png$|BMP$|JPEG$|JPG$|PNG$";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(filename);
        if (!m.find()) {
            return ReturnT.failed(ReturnCodeEnum.IMG_TYPE_EXCEPTION.getCode(),ReturnCodeEnum.IMG_TYPE_EXCEPTION.getMessage());
        }
        //当前时间
        String timeStr = DateFormatUtils.currentDateToStr(DateFormatUtils.formatYYYYMMdd);
        // 上传之后的文件路径  (命名格式为 用户名 + 当前时间)
        userId = userId!=null?userId:"base";
        // 上传之后的文件路径
        String filePath = upLoadPath + File.separator + userId
                + File.separator + timeStr.substring(0,4) + File.separator + timeStr.substring(0,6);
        filename =  DateFormatUtils.currentDateToStr(DateFormatUtils.currentTimeMillis()) +  filename.substring(filename.lastIndexOf("."));
        // 上传文件
        File newFile = new File(filePath,filename);
        // 判断文件夹是否存在，不存在创建
        if(!newFile.getParentFile().exists()){
            newFile.getParentFile().mkdirs();
        }
        //上传图片
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ReturnT.failed(ReturnCodeEnum.IMG_UPLOAD_EXCEPTION.getCode(),ReturnCodeEnum.IMG_UPLOAD_EXCEPTION.getMessage());
        }
        log.info("=============用户图片{}上传成功===============",newFile.getPath());
        return ReturnT.success(newFile.getPath());
    }

    /**
     * 上传图片
     * @param file
     * @param upLoadPath
     * @return
     */
    public static ReturnT<String> uploadImgFile(MultipartFile file,String upLoadPath){
        return uploadImgFile(file,upLoadPath,null);
    }

}
