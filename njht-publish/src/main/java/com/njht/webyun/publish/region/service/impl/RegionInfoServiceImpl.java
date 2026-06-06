package com.njht.webyun.publish.region.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.njht.webyun.constant.DbConstant;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.publish.region.dao.RegionInfoDao;
import com.njht.webyun.publish.region.entity.RegionGeoInfoEntity;
import com.njht.webyun.publish.region.entity.RegionInfoEntity;
import com.njht.webyun.publish.region.service.RegionInfoService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;
import com.njht.webyun.utils.SHPFileReader;
import com.njht.webyun.utils.TreeBuilderUtil;
import com.vividsolutions.jts.geom.Geometry;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * @author daiguojun
 */
@Service("regionInfoService")
@DS(value = DbConstant.MYSQL_1)
public class RegionInfoServiceImpl extends ServiceImpl<RegionInfoDao, RegionInfoEntity> implements RegionInfoService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegionInfoService regionInfoService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RegionInfoEntity> page = this.page(
                new Query<RegionInfoEntity>().getPage(params),
                new QueryWrapper<RegionInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @SneakyThrows
    public Map<String, Object> getGeoInfo(String regionId) {
        Map<String,Object> map = new HashMap<>(16);
        String theGemo = baseMapper.selectGeoByRegionId(regionId);
        if(theGemo != null){
            String json = SHPFileReader.geoToJson(theGemo);
            map.put("geo",objectMapper.readValue(json,Map.class));
            map.put("region",regionId);
        }
        return map;
    }

    @Override
    public List<Tree> getRegionTree() {
        //查询行政区域树集合
        List<RegionInfoEntity> regionList =  baseMapper.selectRegionList();
        //树结构第一级添加全部
        RegionInfoEntity regionInfo = new RegionInfoEntity();
        regionInfo.setRegionid("ALL");
        regionInfo.setFullname("全部");
        regionInfo.setTMRegionid("0");
        regionInfo.setSortKey(0);
        regionList.add(regionInfo);
        //根据sortKey排序
        List<RegionInfoEntity> regionInfoEntities = regionList.stream().sorted(Comparator.comparing(RegionInfoEntity::getSortKey)).collect(Collectors.toList());
        // 将行政区域信息放到树结构中
        List<Tree> treeList = regionInfoEntities.stream().map(regionInfoEntity -> {
            Tree tree = new Tree();
            tree.setParentId(regionInfoEntity.getTMRegionid());
            tree.setLabel(regionInfoEntity.getFullname());
            tree.setValue(regionInfoEntity.getRegionid());
            return tree;
        }).collect(Collectors.toList());
        //构建树结构
        return TreeBuilderUtil.buildTreeList(treeList);
    }

    @Override
    public List<CommonEntity> getRegionListByParentId(String regionId) {
        //根据父id查询行政区域子集信息
        List<RegionInfoEntity> regionInfoEntities = baseMapper.selectRegionListByParentId(regionId);

        //设置返回信息
        return regionInfoEntities.stream().map(regionInfoEntity -> {
            CommonEntity commonEntity = new CommonEntity();
            commonEntity.setLabel(regionInfoEntity.getFullname());
            commonEntity.setValue(regionInfoEntity.getRegionid());
            return commonEntity;
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> getRegionIdListByGeo(Map geo,String regionId) {
        //geo转json
        String geoJson = JSONObject.toJSONString(geo);
        // json 转wkt
        String wkt = SHPFileReader.jsonToWkt(geoJson);
        // wkt 转 geometry
        Geometry geo1 = SHPFileReader.wktToGeo(wkt);
        List<String> regionList = new ArrayList<>();
        List<RegionGeoInfoEntity> geoList = regionInfoService.getGeoInfoList(regionId);
        for(RegionGeoInfoEntity s:geoList){
            Geometry geometry = SHPFileReader.wktToGeo(s.getTheGemo());
            //相交或者包含
            if(geo1.intersects(geometry) || geo1.within(geometry)){
                regionList.add(s.getRegionId());
            }
        }
        return regionList;
    }

    @Override
    public Map<String, String> getRegionIdAndNameMap() {
        List<RegionInfoEntity> list = baseMapper.selectRegionList();
        Map<String,String> map = new ConcurrentHashMap<>(16);
        Collections.synchronizedList(list).stream().parallel().
                forEach(regionInfoEntity -> map.put(regionInfoEntity.getRegionid(),regionInfoEntity.getFullname()));
        return map;
    }


    /**
     * 地图边界信息加缓存
     * @return
     */
    @Cacheable(value = {"regionGeo"},key = "#root.method.name"+"+#regionId")
    @Override
    public List<RegionGeoInfoEntity> getGeoInfoList(String regionId){
        return baseMapper.getGeoInfo(regionId);
    }

    @Override
    public Geometry getWkt(String id) {
        String wkt = baseMapper.selectSimpleGeoByRegionId(id);
        return SHPFileReader.wktToGeo(wkt);
    }

}