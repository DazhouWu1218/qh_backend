package com.njht.webyun.management.order.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.njht.webyun.management.business.dao.BusinessProductDao;
import com.njht.webyun.management.common.constant.Constant;
import com.njht.webyun.management.common.util.CycleType;
import com.njht.webyun.management.common.util.DateUtil;
import com.njht.webyun.management.common.util.SatelliteUtil;
import com.njht.webyun.management.order.dao.ShoppingCartInfoDao;
import com.njht.webyun.management.order.dao.ShoppingCartListDao;
import com.njht.webyun.management.order.entity.ShoppingCartList;
import com.njht.webyun.management.order.entity.ShoppingcartInfo;
import com.njht.webyun.management.order.service.ShoppingCartService;
import com.njht.webyun.management.satellite.vo.SatelliteThematicDataInfoDTO;
import com.njht.webyun.utils.DateFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author lmd
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartListDao shoppingCartListDao;
    @Autowired
    private ShoppingCartInfoDao shoppingCartInfoDao;
    @Autowired
    private BusinessProductDao businessProductDao;


    private static final String TYPE1 = "1";
    private static final String TYPE2 = "2";
    public static final String DATEFORMAT = "yyyyMMddHHmmss";
    public static final String THEMATICDATA = "thematic-data";
    // 卫星专题数据中的图例
    private final String COLORBAR = "colorbar";
    // 卫星专题数据中的大图
    private final String PICTURE = "picture";

    @Value("${sateRegex.colorBar}")
    private String colorBarRegex;

    @Value("${sateRegex.picture}")
    private String picRegex;

    /**
     * 将数据添加到购物车中
     * @param userId
     * @param datas
     * @param type
     * @return
     */
    @Override
    public boolean addGoods(String userId, String type, List<Map<String, String>> datas) throws Exception {
        String identifier = datas.get(0).get("identifier");
        try {
            //是否第一次添加购物车
            if (shoppingCartInfoDao.selectShoppingcart(userId).isEmpty()) {
                shoppingCartInfoDao.insertCartInfo(userId);
                SimpleDateFormat sdf = new SimpleDateFormat(DateFormatUtils.formatYY_MM_dd_ss);
                String cartId = shoppingCartInfoDao.selectShoppingcart(userId).get(0).getId();
                for (Map<String, String> dataMap : datas) {
                    String time = sdf.format(new Date());
                    dataMap.put("cartId", cartId);
                    dataMap.put("time", time);
                }
                if (THEMATICDATA.equals(identifier)) {
                    //表明添加的是专题数据
                    shoppingCartListDao.updateThematicShoppingCartList(datas);
                } else {
                    shoppingCartListDao.updateShoppingCartList(datas);
                }
            } else {
                //查询购物车表获取用户对应的购物车Id
                List<ShoppingcartInfo> list = shoppingCartInfoDao.selectShoppingcart(userId);
                String cartId = list.get(0).getId();
                //获取购物车中未提交的订单数据
                List<ShoppingCartList> listOld = shoppingCartListDao.selectList(cartId);
                List<String> listOldData = new ArrayList<>();
                for (ShoppingCartList shoppingCartList : listOld) {
                    String dataType = shoppingCartList.getDataType();
                    if(dataType==null){
                        dataType="null";
                    }
                    listOldData.add(shoppingCartList.getDataId()+dataType);
                }
                List<Map<String, String>> datas2 = new ArrayList<>();
                //add
                if (TYPE1.equals(type)) {
                    //找到已经添加到购物车的数据
                    for (String listOldDatum : listOldData) {
                        for (Map<String, String> data : datas) {
                            String dataId = data.get("dataId");
                            String dataType = data.get("dataType");
                            if(dataType==null){
                                dataType="null";
                            }
                            if (listOldDatum.equals(dataId+dataType)) {
                                datas2.add(data);
                            }
                        }
                    }
                    datas.removeAll(datas2);
                    if (datas.size() == 0) {
                        return true;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (Map<String, String> dataMap : datas) {
                        String time = sdf.format(new Date());
                        dataMap.put("cartId", cartId);
                        dataMap.put("time", time);
                    }
                    if (THEMATICDATA.equals(identifier)) {
                        //表明添加的是专题数据
                        shoppingCartListDao.updateThematicShoppingCartList(datas);
                    }else if(Constant.SATELLITEDATA.equals(identifier)){
                        updateSateShopCartList(datas);
                    } else {
                        shoppingCartListDao.updateShoppingCartList(datas);
                    }
                }
                if (TYPE2.equals(type)) {
                    List<String> dataIdList = new ArrayList<>();
                    for (Map<String, String> data : datas) {
                        String dataId = data.get("dataId");
                        dataIdList.add(dataId);
                    }
                    shoppingCartListDao.deleteDatas2(cartId, dataIdList);
                }
            }
            return true;

        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }

    }

    /**
     * 卫星数据第二次加购物车
     * @param dataList
     */
    private void updateSateShopCartList(List<Map<String, String>> dataList) {
        List<Map<String, String>> insertDataList = new ArrayList<>();
        for(Map<String,String> item :dataList){
            String dataId = item.get("dataId");
            String cartId = item.get("cartId");
            String dataType = shoppingCartInfoDao.getShoppingInfoByDataId(dataId,cartId);
            boolean b = "0".equals(dataType) && "1".equals(item.get("dataType")) || "1".equals(dataType) && "0".equals(item.get("dataType"));
            //dataType = null 没有入过库直接入库
            if(dataType == null){
                insertDataList.add(item);
            } else if(dataType.equals(item.get("dateType"))){
                continue;
            }else if(b){
                dataType = "0,1";
                shoppingCartInfoDao.updateSatelliteShopCartInfo(dataId,dataType,cartId);
            }else {
                continue;
            }
        }
        //购物车中没有的数据直接新增
        if(insertDataList.size() != 0 ){
            shoppingCartListDao.updateShoppingCartList(insertDataList);
        }
    }

    /**
     * 查询购物车中未添加到订单的数据
     *
     * @param usreId
     * @return
     */
    @Override
    public List<Map<String, Object>> queryShoppingCart(String usreId) throws JsonProcessingException {
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<ShoppingcartInfo> list = shoppingCartInfoDao.selectShoppingcart(usreId);
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        String id = list.get(0).getId();
        List<ShoppingCartList> shoppingCartLists = shoppingCartListDao.queryCartDataIdList(id);
        List<String> issueList = new ArrayList<>();
        for (ShoppingCartList shoppingCartList : shoppingCartLists) {
            String dataId = shoppingCartList.getDataId();
            String identifier = shoppingCartList.getIdentifier();
            String dataType = shoppingCartList.getDataType();
            String tableName = shoppingCartListDao.queryDataTableName(identifier);
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> hashMap = new HashMap<>();
            if (Constant.BUSINESSPRODUCT.equals(identifier + "")) {
                map = shoppingCartListDao.queryBusinessDataList(dataId, tableName);
                //查询产品的第三级名称
                String parentName = shoppingCartListDao.getBusinessProductParentName(dataId);
                String regionName = businessProductDao.selectRegionNameByParameter(String.valueOf(map.get("region_id")));
                String cycle = CycleType.cycleMap.get(map.get("cycle"));
                String name = String.valueOf(map.get("name"));
                String satellite = String.valueOf(map.get("satellite"));
                String resolution = String.valueOf(map.get("resolution"));
                String createTime = String.valueOf(map.get("issue"));
                createTime = createTime.substring(0, 4) + "-" + createTime.substring(4, 6) + "-" + createTime.substring(6, 8) + " " + createTime.substring(8, 10) + ":" + createTime.substring(10, createTime.length());
                hashMap.put("cycleName", cycle);
                hashMap.put("name", parentName+"_"+name);
                hashMap.put("regionName", regionName);
                hashMap.put("resolution", resolution);
                hashMap.put("satellite", satellite);
                hashMap.put("createTime", createTime);
            } else if (Constant.SATELLITEDATA.equals(identifier + "")) {
                map = shoppingCartListDao.querySateDataList(dataId, tableName);
                if (map == null || map.size() <= 0) {
                    return null;
                }
                String satelliteId = map.get("satellite_id").toString();
                String satelliteStatue = SatelliteUtil.getSatelliteStatue(satelliteId);
                hashMap.put("satelliteStatus",satelliteStatue);

                List<String> list1 = Arrays.asList(dataType.split(","));
                List<String> levelList = new ArrayList();
                list1.stream().forEach(item -> {
                    if("0".equals(item)){
                        levelList.add("预处理数据");
                    }
                    if("1".equals(item)){
                        levelList.add("原始数据");
                    }
                });
                String level = levelList.stream().collect(Collectors.joining(","));

                String productId = map.get("product_id").toString();
                String cloud = map.get("cloud").toString();
                String sensorId = map.get("sensor_id").toString();
                String resolution = map.get("resolution").toString();
                //给分辨率加单位
                Integer resolution1 = Integer.valueOf(resolution.substring(0,resolution.indexOf(".")));
                if(resolution1 == 10000){
                    resolution = "10KM";
                }else{
                    resolution = resolution1+"M";
                }
                String time = map.get("date").toString();
                String name = satelliteId + "-" + sensorId + "-" + productId + "-" + time;
                hashMap.put("cloud", cloud);
                map.put("dataType", dataType);
                hashMap.put("name", name);
                hashMap.put("sensorId", sensorId);
                hashMap.put("resolution", resolution);
                hashMap.put("level", level);
                hashMap.put("date", time);
            } else if (Constant.BASICDATA.equals(identifier)) {
                String name = shoppingCartListDao.selectBasicDataList(dataId, tableName);
                name = "青海省" + name;
                hashMap.put("name", name);
                hashMap.put("dataId", dataId);
                map.put("id", dataId);
            } else if (Constant.THEMATICDATA.equals(identifier)) {
                try {
                    String queryTime = shoppingCartList.getQueryTime();
                    String beginTime = queryTime.substring(0, queryTime.indexOf("-")).replace(".", "");
                    String endTime = queryTime.substring(queryTime.indexOf("-") + 1, queryTime.length()).replace(".", "");
                    DateFormat format = new SimpleDateFormat("yyyyMMdd");
                    Date beginDate = format.parse(beginTime);
                    Date endDate = format.parse(endTime);
                    Map<String, Object> totalDataMap = shoppingCartListDao.selectThematicDataList(dataId);
                    String cycle = String.valueOf(totalDataMap.get("cycle"));
                    String cycleName = CycleType.cycleMap.get(cycle);
                    String dataSource = String.valueOf(totalDataMap.get("data_source"));
                    String name = String.valueOf(totalDataMap.get("name"));
                    String mark = String.valueOf(totalDataMap.get("mark"));
                    List<Map<String, Object>> imageDataList = shoppingCartListDao.selectImageData(dataId);

                    List<String> timeList = new ArrayList<>();
                    List<String> theIssueList = new ArrayList<>();
                    imageDataList.stream().forEach(imageDataMap -> {
                        String issue = String.valueOf(imageDataMap.get("issue")).substring(0, 8);
                        try {
                            Date issueDate = format.parse(issue);
                            boolean flag = checkValidDay(beginDate, endDate, issueDate);
                            if (flag) {
                                String date = this.getDateByIssueAndCycle(String.valueOf(imageDataMap.get("issue")), cycle);
                                theIssueList.add(String.valueOf(imageDataMap.get("issue")));
                                 timeList.add(date);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    });
                    hashMap.put("cycle", cycle);
                    hashMap.put("cycleName", cycleName);
                    hashMap.put("dataSource", dataSource);
                    hashMap.put("queryTime", queryTime);
                    hashMap.put("imageNum", theIssueList.size());
                    hashMap.put("name", name);
                    map.put("id",dataId);
                    map.put("time",queryTime);
                    map.put("cycle", cycle);
                    map.put("cycleName", cycleName);
                    map.put("mark", mark);
                    map.put("timeList", timeList);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if(Objects.equals(Constant.SATELLITETHEMATICDATA,identifier)){
                SatelliteThematicDataInfoDTO satelliteThematicDataInfoDTO = shoppingCartListDao.querySatelliteThematicdDataList(dataId, tableName);
                if(satelliteThematicDataInfoDTO != null){
                    map = getDataByDataType(dataType, satelliteThematicDataInfoDTO);
                    hashMap.put("name",map.get("fileName"));
                    hashMap.put("fileType",map.get("fileType"));
                    hashMap.put("dataType",dataType);
                    hashMap.put("spaceResolution",map.get("spaceResolution"));
                    hashMap.put("timeResolution",map.get("timeResolution"));
                    //处理返回结果中的图片信息
                    //数据格式整理
                    Pattern barPattern = Pattern.compile(colorBarRegex);
                    Pattern picPattern = Pattern.compile(picRegex);
                    satelliteThematicDataInfoDTO.getListFiles().stream().forEach(
                            item -> {
                                Matcher matcher = barPattern.matcher(item.getFileName());
                                if(matcher.find()){

                                    hashMap.put("barUrl",item.getFileUrl());
                                }
                                Matcher matcher1 = picPattern.matcher(item.getFileName());
                                if(matcher1.find()){
                                    hashMap.put("pngUrl",item.getFileUrl());
                                }
                            });
                }
            }
            map.put("identifier", identifier);
            map.put("hashMap", hashMap);
            map.put("issueList", issueList);
            dataList.add(map);
        }
        return dataList;
    }
    private Map<String, Object>  getDataByDataType(String dataType, SatelliteThematicDataInfoDTO satelliteThematicDataInfoDTO) {
        String fileName = satelliteThematicDataInfoDTO.getFileName();
        String name = null;
            //原始数据
            if(Objects.equals("1",dataType)){
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
                mapPretreatment.put("id",UUID.randomUUID().toString().replace("-",""));
                mapPretreatment.put("dataId",satelliteThematicDataInfoDTO.getId());
                mapPretreatment.put("dataType",1);
                mapPretreatment.put("cycle",satelliteThematicDataInfoDTO.getCycle());
                String issue = this.getStrDateByIssueAndCycle(satelliteThematicDataInfoDTO.getIssue(), satelliteThematicDataInfoDTO.getCycle());
                mapPretreatment.put("issue",issue);
                mapPretreatment.put("mark",satelliteThematicDataInfoDTO.getMark());
                mapPretreatment.put("fileType",satelliteThematicDataInfoDTO.getFileType());
                mapPretreatment.put("timeResolution",satelliteThematicDataInfoDTO.getTimeResolution());
                mapPretreatment.put("spaceResolution",satelliteThematicDataInfoDTO.getSpaceResolution());
                mapPretreatment.put("listFiles",satelliteThematicDataInfoDTO.getListFiles());
                return mapPretreatment;
                //预处理数据
            }else if(Objects.equals("0",dataType)){
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
                mapOriginal.put("id",UUID.randomUUID().toString().replace("-",""));
                mapOriginal.put("dataId",satelliteThematicDataInfoDTO.getId());
                mapOriginal.put("dataType",0);
                String fileType = satelliteThematicDataInfoDTO.getFileType();
                //前端显示数据类型
                if(Objects.equals("shp",fileType)){
                    mapOriginal.put("fileType",fileType);
                }else {
                    mapOriginal.put("fileType","tif");
                }
                mapOriginal.put("mark",satelliteThematicDataInfoDTO.getMark());
                mapOriginal.put("cycle",satelliteThematicDataInfoDTO.getCycle());
                String issue = this.getStrDateByIssueAndCycle(satelliteThematicDataInfoDTO.getIssue(), satelliteThematicDataInfoDTO.getCycle());
                mapOriginal.put("issue",issue);
                mapOriginal.put("timeResolution",satelliteThematicDataInfoDTO.getTimeResolution());
                mapOriginal.put("spaceResolution",satelliteThematicDataInfoDTO.getSpaceResolution());
                mapOriginal.put("listFiles",satelliteThematicDataInfoDTO.getListFiles());
               return mapOriginal;
            }
            return null;
    }

    public boolean checkValidDay(Date start, Date end, Date check) {
        if ((check.after(start) || check.getTime() == start.getTime()) && (check.before(end) || check.getTime() == end.getTime())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 根据期次以及时间获取到不同格式的时间
     * @param issue
     * @param cycle
     * @return
     */
    private String getDateByIssueAndCycle(String issue, String cycle) {
        Date mon = DateUtil.strToDate(issue, "yyyyMMddHHmm");
        if("COAY".equals(cycle)){
            String yyyy = DateUtil.dateToStr(mon, "yyyy");
            return yyyy;
        }else if("COAQ".equals(cycle) || "COAM".equals(cycle)){
            //季产品
            return DateUtil.dateToStr(mon,"yyyy-MM");
        }else if("COTD".equals(cycle) || "COSD".equals(cycle) || "COED".equals(cycle) || "COOD".equals(cycle) ||"COFD".equals(cycle)){
            //旬周期合成产品
            return DateUtil.dateToStr(mon,"yyyy-MM-dd");
        }else if("COOH".equals(cycle)){
            return DateUtil.dateToStr(mon,"yyyy-MM-dd HH:mm");
        }else if("COTM".equals(cycle) || "COOM".equals(cycle)){
            //COTM  Cycle of Ten Minute 10min周期
            return DateUtil.dateToStr(mon,"yyyy-MM-dd HH:mm:ss");
        }
        return DateUtil.dateToStr(mon,"yyyy-MM-dd HH:mm:ss");
    }

    private String getStrDateByIssueAndCycle(String issue, String cycle) {
        Date mon = DateUtil.strToDate(issue, "yyyyMMdd");
        if("COAY".equals(cycle)){
            String yyyy = DateUtil.dateToStr(mon, "yyyy");
            return yyyy;
        }else if("COAQ".equals(cycle) || "COAM".equals(cycle)){
            //季产品
            return DateUtil.dateToStr(mon,"yyyyMM");
        }else if("COTD".equals(cycle) || "COSD".equals(cycle) || "COED".equals(cycle) || "COOD".equals(cycle) ||"COFD".equals(cycle)){
            //旬周期合成产品
            return DateUtil.dateToStr(mon,"yyyyMMdd");
        }else if("COOH".equals(cycle)){
            return DateUtil.dateToStr(mon,"yyyyMMddHHmm");
        }else if("COTM".equals(cycle) || "COOM".equals(cycle)){
            //COTM  Cycle of Ten Minute 10min周期
            return DateUtil.dateToStr(mon,"yyyyMMddHHmmss");
        }
        return DateUtil.dateToStr(mon,"yyyyMMddHHmmss");
    }

}
