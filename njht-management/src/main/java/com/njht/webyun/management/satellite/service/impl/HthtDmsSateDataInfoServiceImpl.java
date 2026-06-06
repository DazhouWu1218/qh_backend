package com.njht.webyun.management.satellite.service.impl;

import com.alibaba.fastjson.JSON;
import com.njht.webyun.management.common.util.GeoOperation;
import com.njht.webyun.management.common.util.MatchTime;
import com.njht.webyun.management.common.util.SatelliteUtil;
import com.njht.webyun.management.satellite.constant.SatelliteConstant;
import com.njht.webyun.management.satellite.dao.HthtDmsSateDataInfoDao;
import com.njht.webyun.management.satellite.entity.HthtDmsSateDataInfo;
import com.njht.webyun.management.satellite.entity.HthtDmsSateDataInfoVO;
import com.njht.webyun.management.satellite.service.HthtDmsSateDataInfoService;
import com.njht.webyun.management.satellite.vo.*;
import com.njht.webyun.utils.DateFormatUtils;
import com.njht.webyun.utils.SHPFileReader;
import com.vividsolutions.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.sql.Time;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * @author miaoyu
 * @date 2020-05-25 03:19:51
 */
@Service("hthtDmsSateDataInfoService")
public class HthtDmsSateDataInfoServiceImpl implements HthtDmsSateDataInfoService {

    @Autowired
    public HthtDmsSateDataInfoDao hthtDmsSateDataInfoDao;

    private Integer index = 0;
    // 卫星专题数据中的图例
    private final String COLORBAR = "colorbar";
    // 卫星专题数据中的大图
    private final String PICTURE = "picture";

    @Value("${sateRegex.colorBar}")
    private String colorBarRegex;

    @Value("${sateRegex.picture}")
    private String picRegex;

    /**
     * 查询卫星名称树结构
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> selectSateField() throws Exception {
        List<Map<String, Object>> mapList = hthtDmsSateDataInfoDao.selectSateField();
        List<Map<String, Object>> menuList = getMenuList(mapList);
        for (Map<String, Object> stringObjectMap : menuList) {
            index = 0;
            sortList(stringObjectMap);
        }

        return menuList;
    }

    @Override
    public HthtDmsSateDataInfo selectByPrimaryKey(String id) {
        return hthtDmsSateDataInfoDao.selectByPrimaryKey(id);
    }

    @Override
    public Integer deleteByPrimaryKey(Long id) {
        return hthtDmsSateDataInfoDao.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insert(HthtDmsSateDataInfo hthtDmsSateDataInfo) {
        return hthtDmsSateDataInfoDao.insert(hthtDmsSateDataInfo);
    }

    @Override
    public Integer insertSelective(HthtDmsSateDataInfo hthtDmsSateDataInfo) {
        return hthtDmsSateDataInfoDao.insertSelective(hthtDmsSateDataInfo);
    }

    @Override
    public Integer insertSelectiveIgnore(HthtDmsSateDataInfo hthtDmsSateDataInfo) {
        return hthtDmsSateDataInfoDao.insertSelectiveIgnore(hthtDmsSateDataInfo);
    }

    @Override
    public Integer updateByPrimaryKeySelective(HthtDmsSateDataInfo hthtDmsSateDataInfo) {
        return hthtDmsSateDataInfoDao.updateByPrimaryKeySelective(hthtDmsSateDataInfo);
    }

    @Override
    public Integer updateByPrimaryKey(HthtDmsSateDataInfo hthtDmsSateDataInfo) {
        return hthtDmsSateDataInfoDao.updateByPrimaryKey(hthtDmsSateDataInfo);
    }

    @Override
    public Integer batchInsert(List<HthtDmsSateDataInfo> list) {
        return hthtDmsSateDataInfoDao.batchInsert(list);
    }

    @Override
    public Integer batchUpdate(List<HthtDmsSateDataInfo> list) {
        return hthtDmsSateDataInfoDao.batchUpdate(list);
    }

    /**
     * 存在即更新
     *
     * @param
     * @param
     * @return
     */
    @Override
    public Integer upsert(HthtDmsSateDataInfo hthtDmsSateDataInfo) {
        return hthtDmsSateDataInfoDao.upsert(hthtDmsSateDataInfo);
    }

    /**
     * 存在即更新，可选择具体属性
     *
     * @param
     * @return
     */
    @Override
    public Integer upsertSelective(HthtDmsSateDataInfo hthtDmsSateDataInfo) {
        return hthtDmsSateDataInfoDao.upsertSelective(hthtDmsSateDataInfo);
    }

    @Override
    public List<HthtDmsSateDataInfo> query(HthtDmsSateDataInfo hthtDmsSateDataInfo) {
        return hthtDmsSateDataInfoDao.query(hthtDmsSateDataInfo);
    }

    @Override
    public Long queryTotal() {
        return hthtDmsSateDataInfoDao.queryTotal();
    }

    @Override
    public Integer deleteBatch(List<Long> list) {
        return hthtDmsSateDataInfoDao.deleteBatch(list);
    }

    @Override
    public PageInfo<HthtDmsSateDataInfoVO> selectPagedOrderF(Integer pageNum, Integer pageSize, Double resolution, Date beginTime, Date endTime, String sort, List<Map> sateSensorId, Double cloud, List<Integer> dataType, Map geo, String regionId) {
        //高分辨率卫星只有原始数据，低分辨率卫星查询不区分原始预处理
        List<Integer> selectDataType = new ArrayList<>();
        String satelliteId1 = (String)sateSensorId.get(0).get("satelliteId");
        String satelliteStatue = SatelliteUtil.getSatelliteStatue(satelliteId1);
        if(SatelliteUtil.HIGH_RESOLUTION_SATE.equals(satelliteStatue)){
            selectDataType.addAll(dataType);
            dataType.removeAll(dataType);
            dataType.add(1);
        }else{
            selectDataType = null;
        }
        //对象转为json字符串
        String geo1 = null;
        if (geo != null) {
            geo1 = JSON.toJSONString(geo);
        }
        List<Map> list = new ArrayList<>();
        for (Map<String, Object> m : sateSensorId) {
            String satelliteId = m.get("satelliteId").toString();
            List<String> sensorIds = (ArrayList<String>) m.get("sensorIds");
            if (sensorIds != null && sensorIds.size() > 0) {
                for (String sensorId : sensorIds) {
                    Map<String, String> m1 = new HashMap<>();
                    m1.put("satelliteId", satelliteId);
                    m1.put("sensorId", sensorId);
                    list.add(m1);
                }
            } else {
                Map<String, String> m1 = new HashMap<>();
                m1.put("satelliteId", satelliteId);
                list.add(m1);
            }
        }
        List<Map<String, String>> maps = null;
        if (!"default".equals(sort)) {
            maps = new ArrayList<>();
            String[] arr = sort.split(",");
            for (String sor : arr) {
                String[] arrsor = sor.split("-");
                Map<String, String> m = new HashMap<String, String>();
                m.put("sort", arrsor[0]);
                m.put("sortord", arrsor[1]);
                maps.add(m);
            }
        }
        String geostr = null;
        if (geo1 != null && !"".equals(geo1) && !"{}".equals(geo1)) {
            geostr = SHPFileReader.jsonToWkt(geo1);
        }
        boolean f = (geo == null || geo.size() <= 0);
        if (regionId != null && f) {
            List<String> lis = hthtDmsSateDataInfoDao.selectRegionGeoStr(regionId);
            if (lis != null && lis.size() > 0) {
                geostr = lis.get(0);
            }
        }
        PageInfo<HthtDmsSateDataInfoVO> pageInfo = null;

        if (list.size() <= 0) {
            List<HthtDmsSateDataInfoVO> ls = new ArrayList<>();
            pageInfo = new PageInfo<HthtDmsSateDataInfoVO>(ls, 0, pageNum, pageSize, null);
        } else {
            List<HthtDmsSateDataInfoVO> collect2 = hthtDmsSateDataInfoDao.selectPagedOrderF(resolution, beginTime
                    , endTime, list
                    , maps, cloud,selectDataType);
            //数据库查询结果过滤掉没有图片的数据并且给分辨率加单位（m）
            ArrayList<HthtDmsSateDataInfoVO> ls = collect2.stream().filter(item -> item.getPngurl() != null && !"".equals(item.getPngurl())).collect(Collectors.toCollection(ArrayList::new));
            ls.stream().forEach(item -> {
                //给分辨率加单位
                Integer resolution1 = Integer.valueOf(item.getResolution().substring(0,item.getResolution().indexOf(".")));
                if(resolution1 == 10000){
                    item.setResolution("10KM");
                }else{
                    item.setResolution(resolution1+"M");
                }
                //判断卫星是低分辨率卫星还是高分辨率卫星 并返回
                String satelliteStatus = SatelliteUtil.getSatelliteStatue(item.getSatelliteId());
                item.setSatelliteStatus(satelliteStatus);
                String collect = dataType.stream().map(integer -> integer.toString()).collect(Collectors.joining(","));
                item.setDataType(collect);
                List levelList = new ArrayList();
                dataType.stream().forEach(integer -> {
                    if(integer == 0){
                        levelList.add("预处理数据");
                    }
                    if(integer == 1){
                        levelList.add("原始数据");
                    }
                });
                item.setLevel((String)levelList.stream().collect(Collectors.joining(",")));
            });
            List<HthtDmsSateDataInfoVO> lst = new ArrayList<>();
            if (geostr != null) {
                Geometry g1 = SHPFileReader.wktToGeo(geostr);
                for (HthtDmsSateDataInfoVO hthtDmsSateDataInfoVO : ls) {
                    String geowkt = hthtDmsSateDataInfoVO.getThe_geom();
                    Geometry g2 = SHPFileReader.wktToGeo(geowkt);
                    if (g1.intersects(g2)) {
                        lst.add(hthtDmsSateDataInfoVO);
                    }
                }
            } else {
                lst = ls;
            }
            for (HthtDmsSateDataInfoVO hthtDmsSateDataInfoVO : lst) {
                String satelliteId = hthtDmsSateDataInfoVO.getSatelliteId();
                String sensorId = hthtDmsSateDataInfoVO.getSensorId();
                String productId = hthtDmsSateDataInfoVO.getProductId();
                Date date = hthtDmsSateDataInfoVO.getDate();
                hthtDmsSateDataInfoVO.setDay(DateFormatUtils.dateToStr(date,DateFormatUtils.formatYYYYMMdd));
                Time time = hthtDmsSateDataInfoVO.getTime();
                Date date1 =  new Date(time.getTime());
                hthtDmsSateDataInfoVO.setHour(DateFormatUtils.dateToStr(date1,DateFormatUtils.formatHHmm));
            }
            pageInfo = new PageInfo<HthtDmsSateDataInfoVO>(lst, lst.size(), pageNum, pageSize, null);
        }
        return pageInfo;
    }

    @Override
    public Map<String, Object> sumArea(Double resolution, Date beginTime, Date endTime, List<Map> sateSensorId, Double cloud, List<Integer> satelliteType, Map geo, String regionId, Double area) {
        Map<String, Object> returnMap = new HashMap<>();
        String geo1 = null;
        if (geo != null) {
            geo1 = JSON.toJSONString(geo);
        }
        List<Map> list = new ArrayList<>();
        for (Map<String, Object> m : sateSensorId) {
            String satelliteId = m.get("satelliteId").toString();
            List<String> sensorIds = (List<String>) m.get("sensorIds");
            if (sensorIds != null && sensorIds.size() > 0) {
                for (String sensorId : sensorIds) {
                    Map<String, String> m1 = new HashMap<>();
                    m1.put("satelliteId", satelliteId);
                    m1.put("sensorId", sensorId);
                    list.add(m1);
                }
            } else {
                Map<String, String> m1 = new HashMap<>();
                m1.put("satelliteId", satelliteId);
                list.add(m1);
            }
        }
        String geostr = null;
        if (!StringUtils.isEmpty(geo1) && !"{}".equals(geo1)) {
            geostr = SHPFileReader.jsonToWkt(geo1);
        }
        if (regionId != null) {
            List<String> lis = hthtDmsSateDataInfoDao.selectRegionGeoStr(regionId);
            if (lis != null && lis.size() > 0) {
                geostr = lis.get(0);
            }
        }
        if (list.size() <= 0) {
            returnMap.put("areaCoverPercent", 0);
            returnMap.put("areaSceneSum", 0);
            returnMap.put("productSceneSum", 0);
            returnMap.put("productShootAreaSum", 0);
            returnMap.put("areaShootAreaSum", 0);
            return returnMap;
        }
        List<DataInfoStatics> diss = hthtDmsSateDataInfoDao.getAllDataInfo(resolution, beginTime
                , endTime, list
                , cloud);
        List<DataInfoStatics> lst = new ArrayList<>();
        Geometry g1 = null;
        double sumarea = 0.0;
        //所有数据的数量和总面积
        if (geostr != null) {
            g1 = SHPFileReader.wktToGeo(geostr);
            for (DataInfoStatics dataInfoStatics : diss) {
                Geometry g2 = SHPFileReader.wktToGeo(dataInfoStatics.getGeostr());
                if (g1.intersects(g2)) {
                    lst.add(dataInfoStatics);
                    if (dataInfoStatics.getArea() != null) {
                        sumarea = dataInfoStatics.getArea() + sumarea;
                    }
                }
            }
        } else {
            for (DataInfoStatics dataInfoStatics : diss) {
                lst.add(dataInfoStatics);
                if (dataInfoStatics.getArea() != null) {
                    sumarea = dataInfoStatics.getArea() + sumarea;
                }
            }
        }
        int sumsize = lst.size();
        //该类型的数据量和面积
        List<Geometry> typelst = new ArrayList<>();
        double typearea = sumarea;
        int typesize = sumsize;
        Geometry geosum = GeoOperation.combineIntoOneGeometry(typelst);
        if (g1 != null) {
            String percent = GeoOperation.percentage(geosum, g1);
            returnMap.put("areaCoverPercent", Double.parseDouble(percent));
        } else {
            returnMap.put("areaCoverPercent", 0.0);
        }
        returnMap.put("areaSceneSum", sumsize);
        returnMap.put("productSceneSum", typesize);
        returnMap.put("productShootAreaSum", typearea);
        returnMap.put("areaShootAreaSum", sumarea);
        return returnMap;
    }


    @Override
    public List<Map<String, Object>> area(Double resolution, Date beginTime, Date endTime, List<Map> sateSensorId, Double cloud, List<Integer> satelliteType, Map geo, String regionId) {
        String geo1 = null;
        if (geo != null) {
            geo1 = JSON.toJSONString(geo);
        }
        List<Map> list = new ArrayList<>();
        for (Map<String, Object> m : sateSensorId) {
            String satelliteId = m.get("satelliteId").toString();
            List<String> sensorIds = (List<String>) m.get("sensorIds");
            if (sensorIds != null && sensorIds.size() > 0) {
                for (String sensorId : sensorIds) {
                    Map<String, String> m1 = new HashMap<>();
                    m1.put("satelliteId", satelliteId);
                    m1.put("sensorId", sensorId);
                    list.add(m1);
                }
            } else {
                Map<String, String> m1 = new HashMap<>();
                m1.put("satelliteId", satelliteId);
                list.add(m1);
            }
        }
        String geostr = null;
        if (geo1 != null && !"".equals(geo1) && !"{}".equals(geo1)) {
            geostr = SHPFileReader.jsonToWkt(geo1);
        }
        if (regionId != null) {
            List<String> lis = hthtDmsSateDataInfoDao.selectRegionGeoStr(regionId);
            if (lis != null && lis.size() > 0) {
                geostr = lis.get(0);
            }
        }
        PageInfo<HthtDmsSateDataInfoVO> pageInfo = null;
        List<Map<String, Object>> ls1 = new ArrayList<>();
        List<Map<String, Object>> ls = new ArrayList<>();
        if (list.size() <= 0) {

        } else {
            for (int i = 0; i < satelliteType.size(); i++) {
                ls1 = hthtDmsSateDataInfoDao.getArea(resolution, beginTime
                        , endTime, list
                        , cloud, satelliteType.get(i), geostr);
                ls.addAll(ls1);
            }
            ls = ls.stream().distinct().collect(Collectors.toList());

        }
        return ls;
    }

    @Override
    public Map<String, Object> areageo(Double resolution, Date beginTime, Date endTime, List<Map> sateSensorId, Double cloud, List<Integer> dataType, Map geo, String regionId) {
        Map<String, Object> map = new HashMap<String, Object>();
        String geo1 = null;
        if (geo != null) {
            geo1 = JSON.toJSONString(geo);
        }
        List<Map> list = new ArrayList<>();
        for (Map<String, Object> m : sateSensorId) {
            String satelliteId = m.get("satelliteId").toString();
            List<String> sensorIds = (List<String>) m.get("sensorIds");
            if (sensorIds != null && sensorIds.size() > 0) {
                for (String sensorId : sensorIds) {
                    Map<String, String> m1 = new HashMap<>();
                    m1.put("satelliteId", satelliteId);
                    m1.put("sensorId", sensorId);
                    list.add(m1);
                }
            } else {
                Map<String, String> m1 = new HashMap<>();
                m1.put("satelliteId", satelliteId);
                list.add(m1);
            }
        }
        String geostr = null;
        if (geo1 != null && !"".equals(geo1) && !"{}".equals(geo1)) {
            geostr = SHPFileReader.jsonToWkt(geo1);
        }
        if (regionId != null) {
            List<String> lis = hthtDmsSateDataInfoDao.selectRegionGeoStr(regionId);
            if (lis != null && lis.size() > 0) {
                geostr = lis.get(0);
            }
        }
        PageInfo<HthtDmsSateDataInfoVO> pageInfo = null;
        List<String> ls1 = new ArrayList<>();
        List<String> ls = new ArrayList<>();
        if (list.size() <= 0) {
            map.put("geostr", null);
        }
        for (int i = 0; i < dataType.size(); i++) {
            ls1 = hthtDmsSateDataInfoDao.getAreageo(resolution, beginTime
                    , endTime, list
                    , cloud, dataType.get(i));
            ls.addAll(ls1);
        }
        ls = ls.stream().distinct().collect(Collectors.toList());
        List<Geometry> lst = new ArrayList<>();
        if (geostr != null) {
            Geometry g1 = SHPFileReader.wktToGeo(geostr);
            for (String geowkt : ls) {
                Geometry g2 = SHPFileReader.wktToGeo(geowkt);
                if (g1.intersects(g2)) {
                    lst.add(g2);
                }
            }
        } else {
            for (String geowkt : ls) {
                Geometry g2 = SHPFileReader.wktToGeo(geowkt);
                lst.add(g2);
            }
        }
        Geometry geosum = GeoOperation.combineIntoOneGeometry(lst);
        map.put("geostr", JSON.parse(SHPFileReader.geoToJson(geosum)));
        return map;
    }

    @Override
    public Map<String, Object> selectRegion(String id) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = hthtDmsSateDataInfoDao.selectRegion(id);
        if ("0".equals(id)) {
            id = "310000000000";
        }
        List<Map<String, Object>> lis = hthtDmsSateDataInfoDao.selectGeom(id);
        if (lis != null && lis.size() > 0) {
            String json = SHPFileReader.geoToJson(lis.get(0).get("geomstr").toString());
            map.put("geo", JSON.parse(json));
            map.put("region", list);
        }
        return map;
    }

    @Override
    public HthtDmsSateDataInfoVO queryById(String id) {
        List<HthtDmsSateDataInfoVO> lv = hthtDmsSateDataInfoDao.selectByPngId(id);
        HthtDmsSateDataInfoVO vo = null;
        if (lv != null && lv.size() > 0) {
            vo = lv.get(0);
        }
        return vo;
    }


    @Override
    public PageInfo<Map<String,Object>> selectThematicPaged(SelectThematicParam selectThematicParam) {
        List<Integer> dataType = selectThematicParam.getDataType();
        List<Map<String,Object>> listTotal = new ArrayList<>();
        //获取起止和截止时间
        Date beginTime = selectThematicParam.getBeginTime();
        Date endTime = selectThematicParam.getEndTime();
        //时间格式化
        String beginTimeStr = DateFormatUtils.dateToStr(beginTime, DateFormatUtils.formatYYYYMMdd);
        String endTimeStr = DateFormatUtils.dateToStr(endTime, DateFormatUtils.formatYYYYMMdd);
        //获取treeid
        List<String> treeIds = selectThematicParam.getTreeIds();
        List<SatelliteThematicDataInfoDTO> list =  hthtDmsSateDataInfoDao.selectThematicByTreeId(beginTimeStr,endTimeStr,treeIds);
        //数据格式整理
        Pattern barPattern = Pattern.compile(colorBarRegex);
        Pattern picPattern = Pattern.compile(picRegex);
        for (SatelliteThematicDataInfoDTO satelliteThematicDataInfoDTO : list) {
            //处理返回结果中的图片信息
            satelliteThematicDataInfoDTO.getListFiles().stream().forEach(
                    item -> {
                        Matcher matcher = barPattern.matcher(item.getFileName());
                        if(matcher.find()){
                            item.setFileTypeName(COLORBAR);
                        }
                        Matcher matcher1 = picPattern.matcher(item.getFileName());
                        if(matcher1.find()){
                            item.setFileTypeName(PICTURE);
                        }
                    });
            getDataByDataType(dataType,listTotal,satelliteThematicDataInfoDTO);
        }
        //获取当前页和每页条数
        Integer pageNum = selectThematicParam.getPageNum();
        Integer pageSize = selectThematicParam.getPageSize();
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<Map<String, Object>>(listTotal, listTotal.size(), pageNum, pageSize, null);
        return pageInfo;
    }


    private void getDataByDataType(List<Integer> dataType, List<Map<String, Object>> listTotal, SatelliteThematicDataInfoDTO satelliteThematicDataInfoDTO) {
        String fileName = satelliteThematicDataInfoDTO.getFileName();
        String name = null;
        for (Integer integer : dataType) {
            //原始数据
            if(integer==1){
                List<String> newNameList = new ArrayList<>();
                Set<String> newHSet = new LinkedHashSet<>();
                Map<String, Object> mapPretreatment = new HashMap<>(10);
                if(fileName.contains(",")){
                    List<String> list = Arrays.asList(fileName.split(","));
                    for (String s : list) {
                        newNameList.add(s);
                    }
                    for (String s : newNameList) {
                        name = s.substring(0, s.indexOf(".h"));
                        Pattern pattern = Pattern.compile(".*(h\\d{2}v\\d{2}).*");
                        Matcher matcher = pattern.matcher(s);
                        if (matcher.find()) {
                            String group = matcher.group(1);
                            newHSet.add(group);
                        }
                    }
                    String collect = newHSet.stream().collect(Collectors.joining("-"));
                    mapPretreatment.put("fileName",name+"."+collect);
                }else {
                    mapPretreatment.put("fileName",fileName.substring(0,fileName.lastIndexOf(".")));
                }
                mapPretreatment.put("id",satelliteThematicDataInfoDTO.getId());
                mapPretreatment.put("dataId",satelliteThematicDataInfoDTO.getId());
                mapPretreatment.put("dataType",1);
                mapPretreatment.put("fileType",satelliteThematicDataInfoDTO.getFileType());
                String issue = MatchTime.getStrDateByIssueAndCycle(satelliteThematicDataInfoDTO.getIssue(), satelliteThematicDataInfoDTO.getCycle());
                mapPretreatment.put("issue",issue);
                mapPretreatment.put("mark",satelliteThematicDataInfoDTO.getMark());
                mapPretreatment.put("cycle",satelliteThematicDataInfoDTO.getCycle());
                mapPretreatment.put("timeResolution",satelliteThematicDataInfoDTO.getTimeResolution());
                mapPretreatment.put("spaceResolution",satelliteThematicDataInfoDTO.getSpaceResolution());
                mapPretreatment.put("listFiles",satelliteThematicDataInfoDTO.getListFiles());
                //获取图片对应四角坐标
                PngCoordinateInfo pngCoordinateInfo = SatelliteConstant.regionInfoMap.get(satelliteThematicDataInfoDTO.getRegion());
                mapPretreatment.put("pngCoordinateInfo",pngCoordinateInfo);
                listTotal.add(mapPretreatment);
                //预处理数据
            }else if(integer==0){
                Map<String, Object> mapOriginal = new HashMap<>(10);
                List<String> newNameList = new ArrayList<>();
                Set<String> newHSet = new LinkedHashSet<>();
                if(fileName.contains(",")){
                    List<String> list = Arrays.asList(fileName.split(","));
                    for (String s : list) {
                        newNameList.add(s);
                    }
                    for (String s : newNameList) {
                        name = s.substring(0, s.indexOf(".h"));
                    }
                    mapOriginal.put("fileName",name);
                }else {
                    mapOriginal.put("fileName",fileName.substring(0,fileName.lastIndexOf(".")));
                }
                mapOriginal.put("id",satelliteThematicDataInfoDTO.getId()+"id");
                mapOriginal.put("dataId",satelliteThematicDataInfoDTO.getId());
                mapOriginal.put("dataType",0);
                String fileType = satelliteThematicDataInfoDTO.getFileType();
                //前端显示数据类型
                if(Objects.equals("shp",fileType)){
                    mapOriginal.put("fileType",fileType);
                }else {
                    mapOriginal.put("fileType","tif");
                }
                String issue = MatchTime.getStrDateByIssueAndCycle(satelliteThematicDataInfoDTO.getIssue(), satelliteThematicDataInfoDTO.getCycle());
                mapOriginal.put("issue",issue);
                mapOriginal.put("mark",satelliteThematicDataInfoDTO.getMark());
                mapOriginal.put("cycle",satelliteThematicDataInfoDTO.getCycle());
                mapOriginal.put("timeResolution",satelliteThematicDataInfoDTO.getTimeResolution());
                mapOriginal.put("spaceResolution",satelliteThematicDataInfoDTO.getSpaceResolution());
                mapOriginal.put("listFiles",satelliteThematicDataInfoDTO.getListFiles());
                //获取图片对应四角坐标
                PngCoordinateInfo pngCoordinateInfo = SatelliteConstant.regionInfoMap.get(satelliteThematicDataInfoDTO.getRegion());
                mapOriginal.put("pngCoordinateInfo",pngCoordinateInfo);
                listTotal.add(mapOriginal);
            }
        }
    }



    private static float calculate(ArrayList<Point> vertex) {
        int i = 0;
        float temp = 0;
        for (; i < vertex.size() - 1; i++) {
            temp += (vertex.get(i).x - vertex.get(i + 1).x) * (vertex.get(i).y + vertex.get(i + 1).y);
        }
        temp += (vertex.get(i).x - vertex.get(0).x) * (vertex.get(i).y + vertex.get(0).y);
        return temp / 2;
    }

    public List<Map<String, Object>> getMenuList(List<Map<String, Object>> data) {
        // 复制data数据
        List<Map<String, Object>> menuList = new ArrayList(data);
        // 遍历两次data来组装带有children关联性的对象，如果找到子级就删除menuList的数据
        for (Map<String, Object> datum : data) {
            datum.put("child", new ArrayList<Map<String, Object>>());
            String id = String.valueOf(datum.get("id"));
            for (Map<String, Object> stringObjectMap : data) {
                //如果本级id与数据的父id相同，就说明是子父级关系
                String parentId = String.valueOf(stringObjectMap.get("parent_id"));
                if (id.equals(parentId)) {
                    List list = (List<Map<String, Object>>) datum.get("child");
                    list.add(stringObjectMap);
                    menuList.remove(stringObjectMap);
                }
            }
        }
        return menuList;
    }

    public void sortList(Map<String, Object> data) {
        if ((((List<Map<String, Object>>) data.get("child")) == null) || (((List<Map<String, Object>>) data.get("child")).size() <= 0)) {
            String value = String.valueOf(data.get("value"));
            if (value.contains(",")) {
                String[] split = value.split(",");
                data.put("value", split);
            }
            data.put("ids", data.remove("id"));
            data.put("index", index);
            index++;
        } else {
            if ("/".equals(String.valueOf(data.get("value"))) && !"/".equals(String.valueOf(data.get("type")))) {
                data.remove("value");
            } else if ("/".equals(String.valueOf(data.get("value"))) && "/".equals(String.valueOf(data.get("type")))) {
                data.remove("value");
                data.remove("type");
            }
            List<Map<String, Object>> childrenList = (List<Map<String, Object>>) data.get("child");
            for (Map<String, Object> stringObjectMap : childrenList) {
                sortList(stringObjectMap);
            }
        }
    }
}
