package com.njht.webyun.publish.product.dao;

import com.njht.webyun.publish.product.dto.ProductInfoDto;
import com.njht.webyun.publish.product.entity.ProductInfoEntity;
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
public interface ProductInfoDao extends BaseMapper<ProductInfoEntity> {


    /**
     * 根据id集合查询数据源以及对应名称信息
     * @param productIds
     * @return
     */
    List<ProductInfoEntity> selectCycleAndNameInfo(@Param("productIds") List<String> productIds);

    /**
     * 查询模糊搜索需要的信息
     * @return
     * @param treeIdList
     */
    List<ProductInfoEntity> selectProductDistinctList(@Param("treeIdList") List<String> treeIdList);

    /**
     * 查询产品集合
     * @param regionList
     * @param startTime
     * @param endTime
     * @param dataSourceList
     * @param cycleList
     * @param id
     * @return
     */
    List<ProductInfoDto> selectProductInfoList(@Param("regionList") List<String> regionList,
                                               @Param("startTime") String startTime,
                                               @Param("endTime") String endTime,
                                               @Param("dataSourceList") List<String> dataSourceList,
                                               @Param("cycleList") List<String> cycleList,
                                               @Param("productId") String id);

    /**
     * 查询id集合 对应的数据
     * @param idList
     * @return
     */
    List<ProductInfoEntity> selectEntityList(@Param("idList") List<String> idList);
}
