package com.njht.webyun.publish.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.publish.product.entity.ProductFileInfoEntity;
import com.njht.webyun.publish.product.entity.ProductInfoEntity;
import com.njht.webyun.publish.product.vo.ImgInfoReqVo;
import com.njht.webyun.publish.product.vo.ProductFileBaseReqVo;
import com.njht.webyun.publish.product.vo.ProductInfoReqVo;
import com.njht.webyun.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
public interface ProductFileInfoService extends IService<ProductFileInfoEntity> {

    /**
     * 文件下载，返回文件下载路径或者压缩包路径
     * @param ids
     * @param types
     * @return
     */
    String getDownLoadPathByIdList(String ids,String types);

    /**
     * 获取文件数量以及文件类型
     * @param collect
     * @return
     */
    List<ProductInfoReqVo> getFileNumAndTypeList(List<ProductInfoReqVo> collect);


    /**
     * 根据相对路径和绝对路径获取文件压缩图片信息
     * @param relativePath
     * @param filePath
     * @return
     */
    String getFileThumbUrl(String relativePath, String filePath);

    /**
     * 查询这一期数据的文件集合
     *
     * @param id
     * @param  mark
     * @param fileTypeList
     * @return
     */
    List<ProductFileBaseReqVo> getFileInfoList (String id,String mark,List<String> fileTypeList);

    /**
     * 获取专题图以及对应的栅格数据
     * @param ids
     * @return
     */
    List<ImgInfoReqVo> getTifAndJpgByIds(List<String> ids);

    /**
     * 根据ids 以及types 返回要收藏的product_file_info_id
     * @param ids
     * @param types
     * @return
     */
    List<ProductFileInfoEntity> getProductFileInfos(String ids, String types);

    /**
     * 图片合成gif 并下载
     * @param ids
     * @return
     */
    String getDownLoadGifPathByIdList(String ids);

    /**
     * id 集合
     * @param records
     * @return
     */
    Map<String, List<ProductFileBaseReqVo>> getFileInfoListByIds(List<ProductInfoEntity> records);

}

