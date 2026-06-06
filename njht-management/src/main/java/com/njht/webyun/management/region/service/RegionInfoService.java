package com.njht.webyun.management.region.service;

import com.njht.webyun.entity.Tree;
import com.njht.webyun.management.region.entity.RegionInfo;
import com.njht.webyun.utils.ReturnT;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author dgj
 */
public interface RegionInfoService {

    /**
     * 如果是省就获取省的id，如果是市就获取该市下面的所有县id，如果是县就获取县id
     * @param regionId
     * @return
     */
    List<String> getRegions(String regionId);

    /**
     * 获取区域的geo地形图
     * @param regionId
     * @return
     */
    Map<String,Object>  getGeo(String regionId);


    /**
     * 获取青海省省市县三级列表
     * @return
     */
    List<Tree> getRegionList();

    /**
     * 通过父id查询区域信息
     * @param parentId
     * @return
     */
    List<RegionInfo> getRegionByParentId(String parentId);

    /**
     * 通过geo 拿到regionId
     * @param geo
     * @return
     */
    List<String> getRegionByGeo(Map geo);

    /**
     * 上传shp文件获取geoJson
     * shp,zip,rar,7z
     * @param fileList
     * @return
     */
    ReturnT<Object> uploadShp(List<MultipartFile> fileList);

    /**
     * 获取树形结构的行政区域或者生态功能区信息
     * @param type
     * @return
     */
    ReturnT<Object> getRegionTree(Integer type);

    /**
     * 根据行政区域信息获取父级id
     * @param id
     * @return
     */
    ReturnT<Object> getRegionIdInfo(String id);

    /**
     * 服务产品树单独处理
     * @return
     */
    List<Tree> getServiceRegionTree();

    String getWkt(String id);
}
