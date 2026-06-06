package com.njht.webyun.management.business.dao;

import com.njht.webyun.management.business.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author dgj
 */
@Repository
public interface BusinessProductDao {

    /**
     * 通过用户id查询相关的业务产品信息
     * @param userId
     * @return
     */
    List<ProductTree> selectProductTreeByUserId(@Param("userId") String userId);

    /**
     * 按条件查询商品详情
     * @param begin
     * @param end
     * @param fls
     * @param cycles
     * @param dataSources
     * @param regions
     * @param mark
     * @return
     */
    List<ProductInfoDto> selectProductList(@Param("begin") String begin, @Param("end")String end,
                                           @Param("fls")List<String> fls, @Param("cycles")List<String> cycles,
                                           @Param("dataSources")List<String> dataSources,
                                           @Param("regions")List<String> regions,
                                           @Param("mark")List<String> mark);

    /**
     * 查找同一期次的产品信息
     * @param id
     * @return
     */
    List<ProductFileInfo> selectProductFileInfos(@Param("id") String id);

    /**
     * 查询产品周期 去重
     * @param productIds
     * @return
     */
    List<String> selectCycles(@Param("productIds")List<String> productIds);

    /**
     * 查询产品的数据源 去重
     * @param productIds
     * @return
     */
    List<String> selectDataSources(@Param("productIds")List<String> productIds);

    /**
     * 通过treeId查找parentid
     * @param treeId
     * @return
     */
    String selectProductId(@Param("treeId")String treeId);

    /**
     * 查询行政区域id
     * @param region_id
     * @return
     */
    String selectRegionNameByParameter(@Param("regionId") String region_id);

    /**
     * 统计分析拿到tif图
     * @param productId
     * @param cycle
     * @param dataSource
     * @param begin
     * @param end
     * @param regionId
     * @return
     */
    List<String> selectTifPath(@Param("productId")String productId, @Param("cycle") String cycle,
                               @Param("dataSource")String dataSource,@Param("begin") String begin,
                               @Param("end") String end,@Param("regionId")String regionId);

    /**
     * 根据产品id查找产品信息
     * @param productId
     * @return
     */
    Product selectProduct(@Param("productId") String productId);

    /**
     * 查询一段时间内的产品期次
     * @param begin
     * @param end
     * @param productId
     * @param cycle
     * @param dataSource
     * @return
     */
    List<String> getIssues(@Param("begin") String begin, @Param("end")String end, @Param("productId")String productId,
                           @Param("cycle") String cycle, @Param("dataSource")String dataSource);

    /**
     * 查找每个产品对应的周期，数据源
     * @param id
     * @return
     */
    List<ProductInfo> getProductInfoList(@Param("id")String id);

    /**
     * 查询产品对应的文件信息
     * @param id
     * @return
     */
    List<ProductFileInfoForgien> selectProductFileInfo(String id);

    /**
     * 查询快试图信息
     * @param id
     * @return
     */
    PngInfoDto getPngInfo(@Param("id")String id);

    void insert(@Param("id") String id,@Param("issue") String issue);


    void insertFileInfo(@Param("id") String fileId,@Param("filePath") String filePath,@Param("fileUrl") String fileUrl, @Param("issue") String issue,
                        @Param("productId") String id);

    /**
     * 查询产品分类id
     * @param fls
     * @return
     */
    List<String> selectProductInfoIds(@Param("fls") List<String> fls);

    /**
     * 通过id查询父级id集合信息
     * @param id
     * @return
     */
    Map<String, String> getParentInfoList(String id);

    /**
     * 获取产品的单位信息
     * @param id
     * @return
     */
    UnitInfo getUnitInfo(String id);

    /**
     * 查询当天的word文档数据
     * @return
     */
    List<String> getWordList(@Param("issue") String issue);
}
