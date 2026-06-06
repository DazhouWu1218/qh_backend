package com.njht.webyun.publish.region.controller;

import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.publish.region.service.RegionInfoService;
import com.njht.webyun.utils.ReturnT;
import com.vividsolutions.jts.geom.Geometry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 17:11:52
 */
@RestController
@RequestMapping("region/info")
@Api(value = "", tags = "行政区域")
public class RegionInfoController {
    @Autowired
    private RegionInfoService regionInfoService;

    @ApiOperation(value = "分类树结构")
    @GetMapping({"/tree"})
    @ResponseBody
    public ReturnT<List<Tree>> getRegionTree(){
        List<Tree> treeList = regionInfoService.getRegionTree();
        return ReturnT.success(treeList);
    }

    @ApiOperation(value = "根据行政区域获取地图边界信息")
    @ApiImplicitParam(paramType="String", name="id", dataType="String", required=true, value="行政区域id")
    @GetMapping({"/geo"})
    @ResponseBody
    public ReturnT<Map<String, Object>> getGeoInfoByRegionId(@NotNull @RequestParam("id") String regionId){
        Map<String, Object> geoInfo = regionInfoService.getGeoInfo(regionId);
        return ReturnT.success(geoInfo);
    }

    /**
     * 列表
     */
    @ApiOperation(value = "根据行政区域id获取子集信息")
    @ApiImplicitParam(paramType="String", name="id", dataType="String", required=true, value="行政区域id 默认父id 0 ")
    @GetMapping("/list")
    public ReturnT<List<CommonEntity>> list(@NotNull @RequestParam("id") String regionId){
        List<CommonEntity> list = regionInfoService.getRegionListByParentId(regionId);
        return ReturnT.success(list);
    }

    @ApiOperation(value = "根据行政区域id获取地图边界信息")
    @ApiImplicitParam(paramType="String", name="id", dataType="String", required=true, value="行政区域id 默认父id 0 ")
    @GetMapping("getWkt")
    @ResponseBody
    public Map<String,String> getWkt(String id){
        Geometry geometry = regionInfoService.getWkt(id);
        Map<String,String> map = new HashMap<>(1);
        map.put("data",geometry.toText());
        return map;
    }

}
