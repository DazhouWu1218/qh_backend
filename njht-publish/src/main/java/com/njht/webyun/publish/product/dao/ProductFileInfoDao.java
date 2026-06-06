package com.njht.webyun.publish.product.dao;

import com.njht.webyun.publish.product.entity.ProductFileInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
@Mapper
@Repository
public interface ProductFileInfoDao extends BaseMapper<ProductFileInfoEntity> {

    /**
     * 查询文件大小
     * @param idList
     * @return
     */
    List<ProductFileInfoEntity> selectFileTypeList(@Param("idList")List<String> idList);

    /**
     * 查询图片相关信息
     * @param productId
     * @param regionId
     * @return
     */
    List<ProductFileInfoEntity> selectProductFileInfoByTreeId(@Param("id")String productId,@Param("regionId") String regionId);
}
