package com.njht.webyun.product.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.njht.webyun.common.UserUtil;
import com.njht.webyun.constant.DbConstant;
import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.product.constant.ProductConstant;
import com.njht.webyun.product.service.ProductImageService;
import com.njht.webyun.system.model.sysDic.SysDic;
import com.njht.webyun.system.service.inf.SysDicService;
import com.njht.webyun.utils.FileUploadUtils;
import com.njht.webyun.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/24 10:31
 * @Description: 上传下载实现类
 */
@Service
@DS(value = DbConstant.MYSQL_1)
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    private SysDicService sysDicService;

    @Override
    public String uploadFile(MultipartFile file) {
        // 当前用户
        Integer userId = UserUtil.getCurrentUser().getUserId();
        // 获取图片上传路径 以及 基础路径
        SysDic dic = new SysDic();
        dic.setDicType(ProductConstant.PRODUCT_DATA_PATH_TYPE);
        List<SysDic> dicList = new ArrayList<>();
        try {
            dicList = sysDicService.getValuesByType(dic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果没有获取到dic相关信息，直接返回
        if (dicList.isEmpty()) {
            throw new CommMsgException("字典表sys_dic中没有 DIC_TYPE="+ProductConstant.PRODUCT_DATA_PATH_TYPE+" 的相关关信息");
        }
        //上传图片路径
        String upLoadPath = this.getDicValueByKey(dicList,ProductConstant.PRODUCT_DATA_PATH_UPLOAD);
        //上传图片并返回对应的图片存储路径
        ReturnT<String> returnT = FileUploadUtils.uploadImgFile(file, upLoadPath, String.valueOf(userId));
        // 如果状态码为 500 表示上传失败，返回错误吗信息
        if (!Objects.equals(ReturnT.SUCCESS_CODE,returnT.getCode())) {
            throw new CommMsgException(returnT.getMsg());
        }
        // 上传之后的图片路径
        String imagePath = returnT.getData();
        // 如果图片不存在 返回错误信息
        if (! new File(imagePath).exists()) {
            throw new CommMsgException("上传之后的产品图片不存在,路径为："+imagePath);
        }
        //nginx代理路径
        String basePath = this.getDicValueByKey(dicList,ProductConstant.PRODUCT_DATA_PATH_BASE);
        // 返回图片相对路劲，前端通过FileServer代理
        return this.getRelativePath(imagePath,basePath);
    }

    /**
     * 获取图片相对路劲
     * @param imagePath 图片路径
     * @param basePath  基础路径
     * @return
     */
    private String getRelativePath(String imagePath, String basePath) {
        basePath = basePath.replace("\\", "/");
        return imagePath.replace("\\","/").replace(basePath,"");
    }

    /**
     * 从字典表中根据key获取对应Value的信息
     * @param dicList 字典集合
     * @param key  字典key
     * @return
     */
    private String getDicValueByKey(List<SysDic> dicList, String key) {
        String value = null;
        //key 对应的Value集合
        List<String> valueList = dicList.stream().filter(sysDic -> Objects.equals(sysDic.getDicKey(), key)).map(SysDic::getDicValue).collect(Collectors.toList());
        if(!valueList.isEmpty()){
            value = valueList.get(0);
        }else {
            throw new CommMsgException("字典表sys_dic中没有 DIC_TYPE="+ProductConstant.PRODUCT_DATA_PATH_TYPE+", DIC_KEY= "+key+" 的相关关信息");
        }
        return value;
    }
}
