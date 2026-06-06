package com.njht.webyun.publish.region.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.publish.region.entity.RegionGeoInfoEntity;
import com.njht.webyun.publish.region.entity.RegionInfoEntity;
import com.njht.webyun.utils.PageUtils;
import com.vividsolutions.jts.geom.Geometry;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 17:11:52
 */
public interface RegionInfoService extends IService<RegionInfoEntity> {

    /**
     * 查询列表
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据行政区域id 获取边界信息
     * @param regionId
     * @return
     */
    Map<String, Object> getGeoInfo(String regionId);

    /**
     * 行政区域树结构
     * @return
     */
    List<Tree> getRegionTree();

    /**
     * 根据id查询子集信息
     * @param regionId
     * @return
     */
    List<CommonEntity> getRegionListByParentId(String regionId);

    /**
     * 通过geo 拿到regionId
     * @param geo
     * @param regionId
     * @return
     */
    List<String> getRegionIdListByGeo(Map geo,String regionId);

    /**
     * id name 键值对
     * @return
     */
    Map<String, String> getRegionIdAndNameMap();


    /**
     * 查询地图边界范围信息
     * @param regionId
     * @return
     */
    List<RegionGeoInfoEntity> getGeoInfoList(String regionId);

    /**
     * 获取地图边界信息
     * @param id
     * @return
     */
    Geometry getWkt(String id);
}

