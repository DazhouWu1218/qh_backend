package com.njht.webyun.management.region.dao;


import com.njht.webyun.management.region.entity.RegionInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dgj
 */
@Repository
public interface RegionInfoDao {

    /**
     * 通过地区id查找所属地区
     * @param regionId
     * @return
     */
    String  getRegionName(@Param("regionId") String regionId);

    /**
     * 通过regionId 获取region信息
     * @param regionId
     * @return
     */
    RegionInfo getRegion(@Param("regionId") String regionId);

    /**
     * 通过父id查找父id下面的所有区域id
     * @param regionId
     * @return
     */
    List<RegionInfo> getRegionByParentid(@Param("regionId") String regionId);

    /**
     * 修改数据库中地区的geo
     * @param theGemo
     * @param regionId
     */
    void update(@Param("theGemo") String theGemo, @Param("regionId") String regionId);


//    /**
//     * 查询所有县的regionid
//     * @return
//     */
//    List<RegionInfo> getRegionId();

    /**
     * 获取青海省市县列表
     * @return
     */
    List<RegionInfo> getRegionList();

    /**
     * 获取所有市的给信息
     * @return
     */
    List<RegionInfo> getGeo();

    List<String> getAllGeo();


    /**
     * 查询行政区域简称
     * @return
     */
    List<RegionInfo> selectRegionInfo();


    void updateLabel(@Param("regionId") String regionId, @Param("label") String label);

    /**
     * 获取服务产品树结构
     * @return
     */
    List<RegionInfo> getServiceRegionList();

    /**
     * 获取行政区域编码以及简写信息
     * @return
     */
    List<RegionInfo> getLabelAndCodeInfo();


    void insert(@Param("geo") String geo,@Param("regionId") String regionId);

}
