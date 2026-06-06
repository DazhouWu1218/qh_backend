package com.njht.webyun.publish.region.dao;

import com.njht.webyun.publish.region.entity.RegionGeoInfoEntity;
import com.njht.webyun.publish.region.entity.RegionInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 17:11:52
 */
@Mapper
public interface RegionInfoDao extends BaseMapper<RegionInfoEntity> {

    /**
     * 查询地图行政区域边界信息
     * @param regionId
     * @return
     */
    String selectGeoByRegionId(String regionId);


    /**
     * 查询简化版地图行政区域
     * @param regionId
     * @return
     */
    String selectSimpleGeoByRegionId(String regionId);
    /**
     * 查询行政区域集合
     * @return
     */
    List<RegionInfoEntity> selectRegionList();

    /**
     * 根据父id查询子集信息
     * @param regionId
     * @return
     */
    List<RegionInfoEntity> selectRegionListByParentId(String regionId);

    /**
     * 获取省市级 行政区域 以及行政区域边界信息
     * @param regionId
     * @return
     */
    List<RegionGeoInfoEntity> getGeoInfo(@Param("regionId") String regionId);
}
