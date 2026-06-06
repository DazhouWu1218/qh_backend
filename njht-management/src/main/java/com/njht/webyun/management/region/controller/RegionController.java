package com.njht.webyun.management.region.controller;

import com.njht.webyun.entity.Tree;
import com.njht.webyun.management.region.entity.RegionInfo;
import com.njht.webyun.management.region.service.RegionInfoService;
import com.njht.webyun.utils.ReturnT;
import com.njht.webyun.utils.TreeBuilderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author dgj
 */
@RestController
@RequestMapping("region")
public class RegionController {

    @Autowired
    private RegionInfoService regionInfoService;

//    @GetMapping("getRegionTree")
//    @ResponseBody
//    public ReturnT<List<Tree>> getRegionTree(){
//        ReturnT<List<Tree>> returnT = new ReturnT<>();
//        List<Tree> regionInfoList = regionInfoService.getServiceRegionTree();
//        List<Tree> regionInfoTree = TreeBuilder.buildByRecursive1(regionInfoList);
//        returnT.setContent(regionInfoTree);
//        returnT.setCode(ReturnT.SUCCESS_CODE);
//        return returnT;
//    }

    @GetMapping({"getServiceRegionTree","getRegionTree"})
    @ResponseBody
    public ReturnT<List<Tree>> getServiceRegionTree(){
        ReturnT<List<Tree>> returnT = new ReturnT<>();
        List<Tree> regionInfoList = regionInfoService.getServiceRegionTree();
        List<Tree> regionInfoTree = TreeBuilderUtil.buildTreeList(regionInfoList);
        returnT.setData(regionInfoTree);
        returnT.setCode(ReturnT.SUCCESS_CODE);
        return returnT;
    }

    @GetMapping("getWkt")
    @ResponseBody
    public String getWkt(String id){
        return  regionInfoService.getWkt(id);
    }

    @GetMapping("getGeo")
    @ResponseBody
    public ReturnT<Map<String,Object>> getGeo(@RequestParam(value = "regionId") String regionId){
        ReturnT<Map<String,Object>> returnT = new ReturnT<>();
        Map<String, Object> geo = regionInfoService.getGeo(regionId);
        returnT.setData(geo);
        returnT.setCode(ReturnT.SUCCESS_CODE);
        return returnT;
    }

    @GetMapping("getRegionByParentId")
    @ResponseBody
    public ReturnT<List<RegionInfo>> getRegionByParentId(@RequestParam(value = "parentId") String parentId){
        ReturnT<List<RegionInfo>> returnT = new ReturnT<>();
        List<RegionInfo> regionInfos = regionInfoService.getRegionByParentId(parentId);
        returnT.setCode(ReturnT.SUCCESS_CODE);
        returnT.setData(regionInfos);
        return returnT;
    }

    @PostMapping("uploadShp")
    @ResponseBody
    public ReturnT<Object> uploadShp(@RequestParam("file") ArrayList<MultipartFile> file){
        ReturnT<Object> returnT = regionInfoService.uploadShp(file);
        return returnT;
    }


    @GetMapping("getRegionIdInfo")
    @ResponseBody
    public ReturnT<Object> getRegionIdInfo(@RequestParam(value = "id") String id){
        return regionInfoService.getRegionIdInfo(id);
    }
}
