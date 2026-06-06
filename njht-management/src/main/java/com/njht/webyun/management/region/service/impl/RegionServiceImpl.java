package com.njht.webyun.management.region.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.management.common.util.FileUnpackUtil;
import com.njht.webyun.management.region.dao.RegionInfoDao;
import com.njht.webyun.management.region.entity.RegionInfo;
import com.njht.webyun.management.region.service.RegionInfoService;
import com.njht.webyun.utils.FileSearchUtils;
import com.njht.webyun.utils.ReturnT;
import com.njht.webyun.utils.SHPFileReader;
import com.njht.webyun.utils.TreeBuilderUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.geojson.geom.GeometryJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author dgj
 */
@Service("regionInfoService")
public class RegionServiceImpl implements RegionInfoService {

    @Autowired
    private RegionInfoDao regionInfoDao;

    @Value("${upLoadPath}")
    private String upLoadPath;

    @Override
    public List<String> getRegions(String regionId) {
        RegionInfo regionInfo = regionInfoDao.getRegion(regionId);
        List<String> list = new ArrayList<>();
        if(regionInfo == null){
            list.add("QHS");
            return list;
        }

        if("QHS".equals(regionInfo.getTMRegionid())){
            //市
            List<RegionInfo> ls= regionInfoDao.getRegionByParentid(regionInfo.getRegionId());
            for(RegionInfo r:ls){
                list.add(r.getRegionId());
            }
            list.add(regionInfo.getRegionId());
            return list;
        }
        //省和县直接添加自己
        list.add(regionId);

        return list;
    }

    @Override
    public Map<String, Object> getGeo(String regionId) {
        Map<String,Object> map = new HashMap<>(16);
        if(regionId.equals("LYFW") || regionId.equals("STGNQ") || regionId.equals("STQXFZX")){
            regionId = "QHS";
        }
        RegionInfo regionInfo = regionInfoDao.getRegion(regionId);
        String theGemo = regionInfo.getTheGemo();
        if(theGemo != null){
            String json = SHPFileReader.geoToJson(theGemo);
            map.put("geo", JSON.parse(json));
            map.put("region",regionId);
        }
        return map;
    }



    @Override
    public List<Tree> getRegionList() {
        List<RegionInfo> regionInfoList = regionInfoDao.getRegionList();
        List<Tree> tree = changeRegionListToRegionTree(regionInfoList);
        return tree;
    }

    @Override
    public List<RegionInfo> getRegionByParentId(String parentId) {
        return regionInfoDao.getRegionByParentid(parentId);
    }

    @Override
    public List<String> getRegionByGeo(Map geo) {
        String geoJson = JSONObject.toJSONString(geo);
        String wkt = SHPFileReader.jsonToWkt(geoJson);
        Geometry geo1 = SHPFileReader.wktToGeo(wkt);

        List<String> regionList = new ArrayList<>();
        List<RegionInfo> geoList = regionInfoDao.getGeo();
        for(RegionInfo s:geoList){
            Geometry geometry = SHPFileReader.wktToGeo(s.getTheGemo());
            if(geo1.intersects(geometry)){
                regionList.add(s.getRegionId());
            }
        }
        return regionList;
    }

    private String getShpFile(List<MultipartFile> fileList) {
        File shpFile = null;
        String regex = ".*.shp$";
        Pattern p = Pattern.compile(regex);

        for(MultipartFile file:fileList){
            String fileName = file.getOriginalFilename();
            //文件上传之后的路径
            File dest = new File(upLoadPath+File.separator+fileName.substring(0,fileName.lastIndexOf("."))+File.separator+fileName);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            //文件上传到服务器
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            Matcher m =  p.matcher(dest.getName());
            if(m.find()){
                shpFile = dest;
            }
            if(".zip".equals(fileName.substring(fileName.lastIndexOf(".")))){
                File destFile = new File(dest.getParentFile().getParentFile().getPath());
                try {
                    FileUnpackUtil.unpackZip(dest,destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SHPFileReader.delFile(dest);
                //遍历解压之后的文件拿到shp并返回
                List<File> subFiles = FileSearchUtils.getSubFiles(destFile);
                for(File f:subFiles){
                    Matcher m1 = p.matcher(f.getName());
                    if(m1.find()){
                        shpFile = f;
                        break;
                    }
                }
            }
        }
        if(shpFile == null){
            return null;
        }else{
            return shpFile.getPath();
        }
    }

    @Override
    public ReturnT<Object> uploadShp(List<MultipartFile> fileList) {
        ReturnT<Object> returnT = new ReturnT<>();
        if(fileList.size() == 0){
            return ReturnT.failed();
        }
        String shpPath = getShpFile(fileList);
       if(shpPath == null){
           returnT.setCode(ReturnT.FAIL_CODE);
           returnT.setMsg("文件上传失败，请重新选择！");
           returnT.setData("0");
           return returnT;
       }
        List<Polygon> arrayList=new ArrayList<>();
        List<Geometry> li=SHPFileReader.getGeometries(shpPath);
        for (Geometry geometry:li) {
            int nu=geometry.getNumGeometries();
            for (int j = 0; j < nu; j++) {
                Geometry g1=geometry.getGeometryN(j);
                int nu2=g1.getNumGeometries();
                for (int k = 0; k < nu2; k++) {
                    Polygon polygon= (Polygon) g1.getGeometryN(k);
                    arrayList.add(polygon);
                }
            }
        }
        Polygon[] polArr=new Polygon[arrayList.size()];
        arrayList.toArray(polArr);
        MultiPolygon multiPolygon=new MultiPolygon(polArr,new GeometryFactory());
        String geo = null;
        try {
            StringWriter writer = new StringWriter();
            GeometryJSON g = new GeometryJSON();
            g.write(multiPolygon, writer);
            geo = writer.toString();
        } catch (Exception e) {
            returnT.setCode(ReturnT.FAIL_CODE);
            return returnT;
        }
        Map<String,String> geoMap = JSONObject.parseObject(geo,HashMap.class);
        File shpFile = new File(shpPath);
        SHPFileReader.delFile(shpFile.getParentFile());
        return ReturnT.success(geoMap);
    }

    private String STATISTIC_QHS = "QHS";
    private String STATISTIC_STGNQ = "STGNQ";

    @Override
    public ReturnT<Object> getRegionTree(Integer type) {
        List<RegionInfo> regionInfoList = regionInfoDao.getRegionList();
//        //如果是行政区域过滤掉行政区域级别为 11 21 的,如果是生态功能区过滤掉级别为10 20 30 的
//
//        ArrayList<RegionInfo> collect = new ArrayList<>();
//        if(type == 1){
//            //行政区域
//            collect = regionInfoList.stream().filter(regionInfo ->
//                    !"10".equals(regionInfo.getRegionlevel())
//                            && !"20".equals(regionInfo.getRegionlevel())
//                            && !"30".equals(regionInfo.getRegionlevel())).collect(Collectors.toCollection(ArrayList::new));
//        }else if(type == 0){
//            collect = regionInfoList.stream().filter(regionInfo ->
//                    !"11".equals(regionInfo.getRegionlevel())
//                            && !"21".equals(regionInfo.getRegionlevel())).collect(Collectors.toCollection(ArrayList::new));
//        }

        //构建树形结构
        List<Tree> tree = changeRegionListToRegionTree(regionInfoList);
        List<Tree> trees = TreeBuilderUtil.buildTreeList(tree);
        if(type == 0){
            //返回青海省行政区域
            List<Tree> children = trees.stream().filter(tree1 -> tree1.getValue().equals(STATISTIC_QHS)).collect(Collectors.toList());
            return ReturnT.success(children);
        }else if( type ==1 ){
            List<Tree> children = trees.stream().filter(tree1 -> tree1.getValue().equals(STATISTIC_STGNQ)).collect(Collectors.toList());
            return ReturnT.success(children.get(0).getChildren());
        }
        return ReturnT.success(trees);
    }

    @Override
    public ReturnT<Object> getRegionIdInfo(String id) {
        List<RegionInfo> regionList = regionInfoDao.getRegionList();
        List<RegionInfo> list = new ArrayList<>();
        list = getParentId(id,regionList,list);
        List<String> returnList = new ArrayList<>();
        list.stream().sorted(Comparator.comparing(RegionInfo::getRegionlevel)).forEach(regionInfo -> returnList.add(regionInfo.getRegionId()));
        return ReturnT.success(returnList);
    }

    @Override
    public List<Tree> getServiceRegionTree() {
        List<RegionInfo> regionInfoList = regionInfoDao.getServiceRegionList();
        List<Tree> tree = changeRegionListToRegionTree(regionInfoList);
        return tree;
    }

    @Override
    public String getWkt(String id) {
        RegionInfo regionInfo = regionInfoDao.getRegion(id);
        return regionInfo.getTheGemo();
    }

    private List<RegionInfo> getParentId(String id,List<RegionInfo> regionList, List<RegionInfo> list) {
        ArrayList<RegionInfo> collect = regionList.stream().filter(regionInfo -> regionInfo.getRegionId().equals(id)).collect(Collectors.toCollection(ArrayList::new));
        list.addAll(collect);
        if(!"0".equals(collect.get(0).getTMRegionid())){
            getParentId(collect.get(0).getTMRegionid(),regionList,list);
        }
        return list;
    }

    private List<Tree> changeRegionListToRegionTree(List<RegionInfo> regionInfoList){
        List<Tree> treeList = new ArrayList<>();
        for (RegionInfo regionInfo : regionInfoList) {
            Tree tree = new Tree();
            tree.setValue(regionInfo.getRegionId());
            tree.setLabel(regionInfo.getFullname());
            tree.setParentId(regionInfo.getTMRegionid());

//            tree.setVirtualId(regionInfo.getRegionId());
//            tree.setVirtualParentId(regionInfo.gettMRegionid());

            treeList.add(tree);
        }

        return treeList;
    }
}
