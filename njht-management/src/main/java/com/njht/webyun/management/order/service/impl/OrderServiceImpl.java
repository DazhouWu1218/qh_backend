package com.njht.webyun.management.order.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.njht.webyun.common.UserUtil;
import com.njht.webyun.management.common.constant.Constant;
import com.njht.webyun.management.common.util.CycleType;
import com.njht.webyun.management.common.util.MapSortUtil;
import com.njht.webyun.management.order.dao.OrderDao;
import com.njht.webyun.management.order.dao.SateOrderDao;
import com.njht.webyun.management.order.dao.ShoppingCartListDao;
import com.njht.webyun.management.order.entity.OrderInfoParam;
import com.njht.webyun.management.order.service.OrderService;
import com.njht.webyun.management.order.vo.HthtOrderInfo;
import com.njht.webyun.management.order.vo.OrderUserDTO;
import com.njht.webyun.management.order.vo.TreeType;
import com.njht.webyun.management.satellite.vo.SatelliteThematicDataInfoDTO;
import com.njht.webyun.management.satellite.vo.SatelliteThematicFileDataInfoDTO;
import com.njht.webyun.management.sys.service.UserService;
import com.njht.webyun.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.DecimalFormat;
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
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ShoppingCartListDao shoppingCartListDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private SateOrderDao sateOrderDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    /**
     * 生成订单
     *
     * @param dataUse
     * @param orderName
     * @param datas
     * @return
     */
    @Override
    public String submitOrder(String userId, String dataUse, String orderName, List<Map<String, String>> datas) throws Exception {
        // 用户id 从token中去取
        userId = String.valueOf(UserUtil.getCurrentUser().getUserId());
        List<String> userIdList = orderDao.selectByUserId(userId);
        String orderId = "";
        String dataType = "";
        Integer fileNumber = 0;
        /**
         * 获取订单编号*/
        //用户之前有订单
        if (userIdList != null && userIdList.size() > 0) {
            String midOrderId = userIdList.get(0);
            midOrderId = midOrderId.substring(midOrderId.indexOf("_") + 1, midOrderId.lastIndexOf("_"));
            DecimalFormat df = new DecimalFormat("0000");
            String format = df.format(Integer.parseInt(midOrderId) + 1);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateTime = sdf.format(date);
            dateTime = dateTime.replace("-", "");
            orderId = dateTime + "_" + format + "_" + userId;
        } else {
            //用户第一次生成订单
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateTime = sdf.format(date);
            int count = orderDao.queryDataNum(dateTime + " 00:00:00", dateTime + " 23:59:59");
            DecimalFormat df = new DecimalFormat("0000");
            String format = df.format(count + 1);
            dateTime = dateTime.replace("-", "");
            orderId = dateTime + "_" + format + "_" + userId;
        }
        try {
            List<Map<String, Object>> resultList = new ArrayList<>();
            String identifier = datas.get(0).get("identifier");
            List<String> dataIdList = new ArrayList<>();
            List<String> dataList = new ArrayList<>();
            //卫星数据下的专题数据可能出现重复id,用set集合进行进行存储
            Set<String> set = new LinkedHashSet<>();
            List<Map<String, String>> dataIdMapList = new ArrayList<>();
            /**
             * 获取传回数据的id*/
            for (Map<String, String> dataMap : datas) {
                String dataId = dataMap.get("dataId");
                dataIdList.add(dataId);
                set.add(dataId);
                dataMap.put("orderId", orderId);
            }
            if (Constant.THEMATICDATA.equals(identifier)) {
                orderDao.insertOrderThematicDataList(datas);
            } else {
                orderDao.insertOrderDataList(datas);
            }
            if (Constant.SATELLITEDATA.equals(identifier)) {
                resultList = orderDao.querySatelliteDataList(dataIdList);
            } else if (Constant.THEMATICDATA.equals(identifier)) {
                resultList = orderDao.selectThematicDataList(dataIdList);
            } else if (Objects.equals(Constant.SATELLITETHEMATICDATA, identifier)) {
                List<SatelliteThematicDataInfoDTO> list = orderDao.querySatelliteThematicdDataList(set);
                for (SatelliteThematicDataInfoDTO satelliteThematicDataInfoDTO : list) {
                    String json = objectMapper.writeValueAsString(satelliteThematicDataInfoDTO);
                    Map<String, Object> map = objectMapper.readValue(json, Map.class);
                    resultList.add(map);
                }
            } else {
                Map<String, String> tableNameColumn = orderDao.queryTableNameColumn(identifier);
                resultList = orderDao.queryTotalDataList(tableNameColumn.get("data_table_name"), tableNameColumn.get("table_column"), dataIdList);
            }
            /**
             * 获取文件的大小
             */
            long size = 0;
            String fileSize = "";
            if (Constant.BASICDATA.equals(identifier)) {
                size = orderDao.selectFileSize(datas);
            } else if (Constant.SATELLITEDATA.equals(identifier)) {
                size = this.getSatelliteDataSize(resultList,datas);
            } else if (Constant.BUSINESSPRODUCT.equals(identifier)) {
                for (Map<String, Object> stringObjectMap : resultList) {
                    fileSize = (String) stringObjectMap.get("file_size");
                    if ( StringUtils.isNotBlank(fileSize) && fileSize.contains(".")) {
                        fileSize = fileSize.substring(0, fileSize.lastIndexOf("."));
                    } else {
                        continue;
                    }
                    size += Long.parseLong(fileSize);
                }
            } else if (Objects.equals(Constant.SATELLITETHEMATICDATA, identifier)) {
                for (Map<String, String> data : datas) {
                    String dataId = data.get("dataId").toString();
                    for (Map<String, Object> map : resultList) {
                        String id = map.get("id").toString();
                        if (Objects.equals(dataId, id)) {
                            String dataTypeMap = data.get("dataType").toString();
                            String fileOriginalSize = map.get("fileSize").toString();
                            long fileOriginalSizeSum = Long.parseLong(fileOriginalSize);
                            Long pretreatmentFileSizeSum = 0L;
                            List<Map<String, Object>> listFiles = (List<Map<String, Object>>) map.get("listFiles");
                            for (Map<String, Object> mapFile : listFiles) {
                                long filePretreatmentSize = Long.parseLong(mapFile.get("fileSize").toString());
                                pretreatmentFileSizeSum += filePretreatmentSize;
                            }
                            //获取原始的数据的大小
                            if (Objects.equals("1", dataTypeMap)) {
                                if (map.get("fileName").toString().contains(",")) {
                                    fileNumber = fileNumber + 2;
                                } else {
                                    fileNumber = fileNumber + 1;
                                }
                                size += fileOriginalSizeSum;
                                //获取预处理数据的大小
                            } else if (Objects.equals("0", dataTypeMap)) {
                                size += pretreatmentFileSizeSum;
                                fileNumber += listFiles.size();
                                //获取预处理和原始数据的大小
                            }
                            break;
                        } else {
                            continue;
                        }
                    }
                }
            } else {
                List<String> sizeList = new ArrayList<>();
                //获取时间范围内的所有文件大小
                resultList.stream().parallel().forEach(resultMap -> {
                    String thematicFileId = String.valueOf(resultMap.get("thematic_file_id"));
                    String issue = String.valueOf(resultMap.get("issue")).substring(0, 8);
                    String midFileSize = String.valueOf(resultMap.get("file_size"));
                    datas.stream().parallel().forEach(dataMap -> {
                        String dataId = dataMap.get("dataId");
                        String queryTime = dataMap.get("queryTime");
                        String beginTime = queryTime.substring(0, queryTime.indexOf("-")).replace(".", "");
                        String endTime = queryTime.substring(queryTime.indexOf("-") + 1, queryTime.length()).replace(".", "");
                        DateFormat format = new SimpleDateFormat("yyyyMMdd");
                        try {
                            Date beginDate = format.parse(beginTime);
                            Date endDate = format.parse(endTime);
                            if (thematicFileId.equals(dataId)) {
                                Date issueDate = format.parse(issue);
                                boolean flag = checkValidDay(beginDate, endDate, issueDate);
                                if (flag) {
                                    dataList.add(String.valueOf(resultMap.get("thematic_file_id")));
                                    sizeList.add(String.valueOf(midFileSize));
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    });
                });
                for (String s : sizeList) {
                    size += Long.parseLong(s);
                }
            }
            if(size != 0) {
                if (Constant.BASICDATA.equals(identifier)) {
                    fileSize = FormetFileSize2(size);
                } else {
                    fileSize = FormetFileSize(size);
                }
                fileSize = FormetFileSize(size);
            }

            String realName = userService.queryRealNameByUserId(Integer.valueOf(userId));

            // 判断数据分类
            if (Constant.THEMATICDATA.equals(identifier)) {
                orderDao.insertOrderInfoData(orderId, orderName, dataUse, userId, realName, dataList.size(), fileSize);
            } else if (Constant.SATELLITETHEMATICDATA.equals(identifier)) {
                orderDao.insertOrderInfoData(orderId, orderName, dataUse, userId, realName, fileNumber, fileSize);
            } else {
                orderDao.insertOrderInfoData(orderId, orderName, dataUse, userId, realName, datas.size(), fileSize);
            }
            /* ------------------------------- 免审核(自动审核) -------------------------------*/
            // TODO 自动审核 暂时注释
            autoApprove(userId, orderId, identifier);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return orderId;
    }

    /**
     * 计算下载卫星数据大小
     * @param resultList
     * @param datas
     * @return
     */
    private long getSatelliteDataSize(List<Map<String, Object>> resultList, List<Map<String, String>> datas) {
        long size = 0L;
        for (Map<String, Object> stringObjectMap : resultList) {
            String  fileSize = (String)stringObjectMap.get("source_file_size");
            if (StringUtils.isNotBlank(fileSize) && fileSize.contains(".")) {
                fileSize = fileSize.substring(0, fileSize.lastIndexOf("."));
            } else {
                continue;
            }
            size += Long.parseLong(fileSize);
        }
        return size;
    }


    /**
     * 免审核(自动审核)
     *
     * @param username
     * @param orderId
     */
    public void autoApprove(String username, String orderId, String identifier) {
//        final List<AuthorityEntity> authorityEntities = authorityService.getAuthorityByUserName(username);
//        // 获取审批权限
//        final Pattern pattern = Pattern.compile(".{1,100}?/auto-approve\\((.{1,100})\\)$");
//        // 可以自动审批的数据
//        final HashSet<String> autoApproveDataTypeSet = new HashSet<>();
//        for (AuthorityEntity authorityEntity : authorityEntities) {
//            final Matcher matcher = pattern.matcher(authorityEntity.getAuthorityPath());
//            if (authorityEntity.getAssertRoute() == false && matcher.find()) {
//                final String autoApproveDataType = matcher.group(1);
//                autoApproveDataTypeSet.add(autoApproveDataType);
//            }
//        }
//        if (autoApproveDataTypeSet.contains(identifier)) {
//            orderDao.checkOrder(orderId, 2, null);
//        }
    }


    /**
     * 查询订单
     *
     * @param userId
     * @param state
     * @param orderState
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public Map<String, Object> queryUserOrder(String userId, String systemUserId, String state, String orderState, String beginTime, String endTime) throws Exception {
//        根据角色信息判断是否有产看订单的权限，有查看哪些订单的权限（获取角色对应的权限）
        String currentUserId =  String.valueOf(UserUtil.getCurrentUser().getUserId());
        if(!systemUserId.equals(currentUserId)){
            systemUserId = currentUserId;
        }
        List<String> authList = Optional.ofNullable(orderDao.getUserAuthorityInfo(UserUtil.getCurrentUser().getUserId())).orElse(new ArrayList<>());
        if (authList.contains(Constant.SATELLITEDATA)) {
            authList.add(Constant.SATELLITETHEMATICDATA);
        }
        //根据开始结束时间查询订单信息,然后根据权限过滤订单信息信息
        List<HthtOrderInfo> list = orderDao.queryByCondition(null, state, orderState, beginTime, endTime);
        //根据id，过滤到当前系统自己订单信息
        String finalSystemUserId = systemUserId;
        ArrayList<HthtOrderInfo> selfOrderList = list.stream().filter(item -> finalSystemUserId.equals(item.getUserId())).collect(Collectors.toCollection(ArrayList::new));
        //再根据权限过滤出可以查阅的订单
        ArrayList<HthtOrderInfo> authOrderList
                = list.stream().filter(item -> authList.contains(item.getIdentifier())).collect(Collectors.toCollection(ArrayList::new));
        //把所有的订单放一起(该订单就是用户可查询的所有订单)
        authOrderList.addAll(selfOrderList);
        //orderList 为当前登录账号可以查看到的所有订单
        ArrayList<HthtOrderInfo> orderList =
                authOrderList.stream()
                        .filter(MapSortUtil.distinctByKey(HthtOrderInfo::getId))
                        .map(item -> {
                            // 1可见可点，2可见不可点
                            if(authList.contains(item.getIdentifier())){
                                item.setOperation(1);
                            }else{
                                item.setOperation(2);
                            }
                            return item;
                        }).collect(Collectors.toCollection(ArrayList::new));
        List<OrderUserDTO> userList = new ArrayList<>();
        OrderUserDTO orderUserDTO = new OrderUserDTO(UUID.randomUUID().toString(), "全部", "");
        userList.add(orderUserDTO);
        for (HthtOrderInfo item : orderList) {
            String userId1 = item.getUserId();
            String realName = item.getRealName();
            OrderUserDTO orderUserDto1 = new OrderUserDTO(userId1, realName, userId1);
            userList.add(orderUserDto1);
        }
        //去重,排序
        ArrayList<OrderUserDTO> userNameList = userList.stream().filter(MapSortUtil.distinctByKey(OrderUserDTO::getValue)).collect(Collectors.toCollection(ArrayList::new));
        Map<String, Object> map = new HashMap<>(16);
        map.put("userNameList", userNameList);
        if (userId == null || "".equals(userId)) {
            map.put("orderList", orderList);
        } else {
            ArrayList<HthtOrderInfo> userSelfOrderList = orderList.stream().filter(item -> userId.equals(item.getUserId())).collect(Collectors.toCollection(ArrayList::new));
            map.put("orderList", userSelfOrderList);
        }
        return map;
    }


    /**
     * 审核订单
     *
     * @param orderId
     * @param orderState
     * @param orderReason
     */
    @Override
    public void checkOrder(String orderId, String orderState, String orderReason) {
        int parseInt = Integer.parseInt(orderState);
        orderDao.checkOrder(orderId, parseInt, orderReason);
    }

    /**
     * 查询订单详情
     *
     * @param orderId
     * @return
     */
    @Override
    public List<Map<String, Object>> queryOrderDetail(String orderId) {
        List<Map<String, Object>> dataTableName = orderDao.queryTableNameByOrderId(orderId);
        List<Map<String, Object>> dataList = new ArrayList<>();

        for (Map<String, Object> stringStringMap : dataTableName) {
            Map<String, Object> dataMap = new HashMap<>(16 );
            List<Map<String, Object>> dataMapList = new ArrayList<>();
            String dataId = String.valueOf(stringStringMap.get("data_id"));
            String tableName = String.valueOf(stringStringMap.get("table_name"));
            String menuId = String.valueOf(stringStringMap.get("menu_id"));
            String queryTime = String.valueOf(stringStringMap.get("query_time"));
            String dataType = String.valueOf(stringStringMap.get("data_type"));
            try {
                //查询业务产品数据详情
                if (Constant.BUSINESSPRODUCT.equals(menuId)) {
                    dataMapList = orderDao.queryBusinessDataByDataTableName(dataId);
                    List<Map<String, String>> fieldList = new ArrayList<>();
                    if (dataMapList == null || dataMapList.isEmpty()) {
                        return Collections.emptyList();
                    }
                    String name = String.valueOf(dataMapList.get(0).get("name"));
                    String cycle = String.valueOf(dataMapList.get(0).get("cycle"));
                    String satellite = String.valueOf(dataMapList.get(0).get("satellite"));
                    String businessName = name + "-" + getycleName(cycle) + "-" + satellite;

                    String issue = String.valueOf(dataMapList.get(0).get("issue"));
                    String mark = String.valueOf(dataMapList.get(0).get("mark"));
                    String businessTime = issue.substring(0, 4) + "-" + issue.substring(4, 6) + "-" + issue.substring(6, 8) + " " + issue.substring(8, 10) + ":" + issue.substring(10, issue.length());
                    String resolution = String.valueOf(dataMapList.get(0).get("resolution"));
                    String businessArea = String.valueOf(dataMapList.get(0).get("fullname"));

                    dataMap.put("businessName", businessName);
                    dataMap.put("businessTime", businessTime);
                    dataMap.put("businessArea", businessArea);
                    dataMap.put("resolution", resolution);
                    dataMap.put("identifier", menuId);
                    dataMap.put("dataId", dataId);

                    dataMap.put("fileNum", String.valueOf(dataMapList.size()));

                    long totFileSize = 0;
                    String dataFormat = "";
                    int i = 0;
                    for (Map<String, Object> stringObjectMap : dataMapList) {
                        Map<String, String> dataMidMap = new HashMap<>();
                        totFileSize += Long.parseLong(String.valueOf(stringObjectMap.get("file_size")));
                        String fileName = String.valueOf(stringObjectMap.get("file_name"));
                        String subDataId = String.valueOf(stringObjectMap.get("sub_data"));
                        String fileSize = FormetFileSize(Long.parseLong(String.valueOf(stringObjectMap.get("file_size"))));
                        String relativePath = String.valueOf(stringObjectMap.get("relative_path"));
                        String fileType = String.valueOf(stringObjectMap.get("file_type"));
                        dataMidMap.put("fileName", fileName);
                        dataMidMap.put("dataId", subDataId);
                        dataMidMap.put("fileSize", fileSize);
                        if (relativePath.endsWith(".docx") || relativePath.endsWith(".doc")) {
                            relativePath = relativePath.replace((relativePath.substring(relativePath.lastIndexOf("."), relativePath.length())), ".pdf");
                        }
                        dataMidMap.put("filePath", relativePath);
                        dataMidMap.put("cycle", cycle);
                        dataMidMap.put("mark", mark);
                        dataMidMap.put("issue", issue);
                        fieldList.add(dataMidMap);
                        if (i < dataMapList.size() - 1) {
                            dataFormat += fileType + "/";
                        } else {
                            dataFormat += fileType;
                        }
                        i++;
                    }
                    String totalFileSize = FormetFileSize(totFileSize);
                    dataMap.put("totalFileSize", totalFileSize);
                    dataMap.put("dataFormat", dataFormat);
                    dataMap.put("fieldList", fieldList);
                    dataList.add(dataMap);
                } else if (Constant.BASICDATA.equals(menuId)) {
                    //查询基础数据数据详情
                    dataMapList = orderDao.queryBasicDataByDataTableName(dataId, tableName);
                    if (dataMapList == null || dataMapList.size() <= 0) {
                        return null;
                    }
                    Map<String, Object> stringObjectMap = dataMapList.get(0);
                    String dataName = String.valueOf(stringObjectMap.get("name"));
                    long size = Long.parseLong(String.valueOf(stringObjectMap.get("file_size")));
                    String fileSize = FormetFileSize(size);
                    dataMap.put("dataName", dataName);
                    dataMap.put("fileSize", fileSize);
                    dataMap.put("treeId", dataId);
                    dataMap.put("dataType", "shp");
                    dataMap.put("identifier", menuId);
                    String treeName = TreeType.treeMap.get(dataId);
                    if (treeName.contains(",")) {
                        String[] treeSplit = treeName.split(",");
                        dataMap.put("treeName", treeSplit);
                    } else {
                        String[] treeSplit = {treeName};
                        dataMap.put("treeName", treeSplit);
                    }
                    dataList.add(dataMap);
                } else if (Constant.SATELLITEDATA.equals(menuId)) {
                    //查询卫星数据数据详情
                    dataMapList = orderDao.querySatelliteDataList2(dataId);
                    if (dataMapList == null || dataMapList.size() <= 0) {
                        return null;
                    }
                    Map<String, Object> stringObjectMap = dataMapList.get(0);
                    String dataName = String.valueOf(stringObjectMap.get("data_name"));
                    String date = String.valueOf(stringObjectMap.get("date"));
                    String pngUrl = String.valueOf(stringObjectMap.get("png_url"));
                    String theGeom = String.valueOf(stringObjectMap.get("the_geom"));
                    String pngtopleftlatitude = String.valueOf(stringObjectMap.get("pngtopleftlatitude"));
                    String pngtopleftlongitude = String.valueOf(stringObjectMap.get("pngtopleftlongitude"));
                    String pngtoprightlatitude = String.valueOf(stringObjectMap.get("pngtoprightlatitude"));
                    String pngtoprightlongitude = String.valueOf(stringObjectMap.get("pngtoprightlongitude"));
                    String pngbottomleftlatitude = String.valueOf(stringObjectMap.get("pngbottomleftlatitude"));
                    String pngbottomleftlongitude = String.valueOf(stringObjectMap.get("pngbottomleftlongitude"));
                    String pngbottomrightlatitude = String.valueOf(stringObjectMap.get("pngbottomrightlatitude"));
                    String pngbottomrightlongitude = String.valueOf(stringObjectMap.get("pngbottomrightlongitude"));

                    String resolution = String.valueOf(stringObjectMap.get("resolution"));
                    String cloud = String.valueOf(stringObjectMap.get("cloud")) + "%";
                    int level = Integer.parseInt(String.valueOf(stringObjectMap.get("data_type")));
                    if (level == 1) {
                        String dataLevel = "原始";
                        dataMap.put("dataLevel", dataLevel);
                    } else {
                        String dataLevel = "预处理";
                        dataMap.put("dataLevel", dataLevel);
                    }
                    long size = Long.parseLong(String.valueOf(stringObjectMap.get("source_file_size")));
                    String fileSize = FormetFileSize(size);
                    dataMap.put("dataName", dataName);
                    dataMap.put("id", dataId);
                    dataMap.put("pngurl", pngUrl);
                    dataMap.put("pngtopleftlatitude", Double.parseDouble(pngtopleftlatitude));
                    dataMap.put("pngtopleftlongitude", Double.parseDouble(pngtopleftlongitude));
                    dataMap.put("pngtoprightlatitude", Double.parseDouble(pngtoprightlatitude));
                    dataMap.put("pngtoprightlongitude", Double.parseDouble(pngtoprightlongitude));
                    dataMap.put("pngbottomleftlatitude", Double.parseDouble(pngbottomleftlatitude));
                    dataMap.put("pngbottomleftlongitude", Double.parseDouble(pngbottomleftlongitude));
                    dataMap.put("pngbottomrightlatitude", Double.parseDouble(pngbottomrightlatitude));
                    dataMap.put("pngbottomrightlongitude", Double.parseDouble(pngbottomrightlongitude));
                    dataMap.put("date", date);
                    dataMap.put("theGeom", theGeom);
                    dataMap.put("resolution", resolution);
                    dataMap.put("cloud", cloud);
                    dataMap.put("fileSize", fileSize);
                    dataMap.put("identifier", menuId);
                    dataList.add(dataMap);
                } else if (Constant.SATELLITETHEMATICDATA.equals(menuId)) {
                    //查询卫星数下的专题数据详情
                    SatelliteThematicDataInfoDTO satelliteThematicDataInfoDTO = orderDao.querySatelliteThematicdDataTableName(dataId);
                    Map<String, Object> dataByDataType = getDataByDataType(dataType, satelliteThematicDataInfoDTO);
                    if (dataByDataType == null) {
                        return null;
                    }
                    dataByDataType.put("identifier", menuId);
                    dataList.add(dataByDataType);
                } else {
                    //查询专题数据订单详情
                    dataMapList = orderDao.queryThematicDataByDataTableName(dataId);
                    if (dataMapList == null || dataMapList.size() <= 0) {
                        return null;
                    }
                    if (queryTime == null) {
                        return null;
                    }
                    for (Map<String, Object> midDataMap : dataMapList) {
                        dataMap = new HashMap<>();
                        String beginTime = queryTime.substring(0, queryTime.indexOf("-")).replace(".", "");
                        String endTime = queryTime.substring(queryTime.indexOf("-") + 1, queryTime.length()).replace(".", "");
                        DateFormat format = new SimpleDateFormat("yyyyMMdd");
                        String issue = String.valueOf(midDataMap.get("issue")).substring(0, 8);
                        String midFileSize = String.valueOf(midDataMap.get("file_size"));
                        String fileType = String.valueOf(midDataMap.get("file_type"));
                        String cycle = String.valueOf(midDataMap.get("cycle"));
                        String mark = String.valueOf(midDataMap.get("mark"));
                        try {
                            Date beginDate = format.parse(beginTime);
                            Date endDate = format.parse(endTime);
                            Date issueDate = format.parse(issue);
                            boolean flag = checkValidDay(beginDate, endDate, issueDate);
                            if (flag) {
                                dataMap.put("dataName", midDataMap.get("file_name"));
                                dataMap.put("dataTime", issue);
                                long size = Long.parseLong(midFileSize);
                                String fileSize = FormetFileSize(size);
                                dataMap.put("dataSize", fileSize);
                                dataMap.put("fileType", fileType);
                                dataMap.put("cycle", cycle);
                                dataMap.put("mark", mark);
                                dataMap.put("identifier", menuId);
                                dataList.add(dataMap);

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }

    private Map<String, Object> getDataByDataType(String dataType, SatelliteThematicDataInfoDTO satelliteThematicDataInfoDTO) {
        String fileName = satelliteThematicDataInfoDTO.getFileName();
        String name = null;
        //原始数据
        if (Objects.equals("1", dataType)) {
            List<String> newNameList = new ArrayList<>();
            Set<String> newHSet = new LinkedHashSet<>();
            Map<String, Object> mapPretreatment = new HashMap<>(10);
            if (fileName.contains(",")) {
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
                mapPretreatment.put("fileName", name + "." + collect);
            } else {
                mapPretreatment.put("fileName", fileName.substring(0, fileName.lastIndexOf(".")));
            }
            mapPretreatment.put("id", UUID.randomUUID().toString().replace("-", ""));
            mapPretreatment.put("dataId", satelliteThematicDataInfoDTO.getId());
            mapPretreatment.put("dataType", 1);
            mapPretreatment.put("fileType", satelliteThematicDataInfoDTO.getFileType());
            mapPretreatment.put("timeResolution", satelliteThematicDataInfoDTO.getTimeResolution());
            mapPretreatment.put("spaceResolution", satelliteThematicDataInfoDTO.getSpaceResolution());
            mapPretreatment.put("level", "原始");
            String fileSize = FormetFileSize(satelliteThematicDataInfoDTO.getFileSize());
            mapPretreatment.put("fileSize", fileSize);
            List<SatelliteThematicFileDataInfoDTO> listFiles = satelliteThematicDataInfoDTO.getListFiles();
            for (SatelliteThematicFileDataInfoDTO listFile : listFiles) {
                String fileType = listFile.getFileType();
                if (Objects.equals(fileType, "png")) {
                    mapPretreatment.put("pngUrl", listFile.getFileUrl());
                }
            }
            return mapPretreatment;
            //预处理数据
        } else if (Objects.equals("0", dataType)) {
            Map<String, Object> mapOriginal = new HashMap<>(10);
            List<String> newNameList = new ArrayList<>();
            Set<String> newHSet = new LinkedHashSet<>();
            if (fileName.contains(",")) {
                List<String> list = Arrays.asList(fileName.split(","));
                for (String s : list) {
                    newNameList.add(s);
                }
                for (String s : newNameList) {
                    name = s.substring(0, s.indexOf(".h"));
                }
                mapOriginal.put("fileName", name);
            } else {
                mapOriginal.put("fileName", fileName.substring(0, fileName.lastIndexOf(".")));
            }
            mapOriginal.put("id", UUID.randomUUID().toString().replace("-", ""));
            mapOriginal.put("dataId", satelliteThematicDataInfoDTO.getId());
            mapOriginal.put("dataType", 0);
            mapOriginal.put("level", "预处理");
            mapOriginal.put("fileType", "tif");
            mapOriginal.put("timeResolution", satelliteThematicDataInfoDTO.getTimeResolution());
            mapOriginal.put("spaceResolution", satelliteThematicDataInfoDTO.getSpaceResolution());
            List<SatelliteThematicFileDataInfoDTO> listFiles = satelliteThematicDataInfoDTO.getListFiles();
            Long size = 0L;
            for (SatelliteThematicFileDataInfoDTO listFile : listFiles) {
                String fileType = listFile.getFileType();
                if (Objects.equals(fileType, "png")) {
                    mapOriginal.put("pngUrl", listFile.getFileUrl());
                } else if (Objects.equals(fileType, "tif")) {
                    size += listFile.getFileSize();
                }
            }
            String fileSize = FormetFileSize(size);
            mapOriginal.put("fileSize", fileSize);
            return mapOriginal;
        }
        return null;
    }

    @Override
    public void delOrderInNums(OrderInfoParam orderInfoParam) {
        List<String> orderIds = orderInfoParam.getOrderIds();
        orderDao.delOrderList(orderIds);
        orderDao.delOrderInfo(orderIds);
    }

    /**
     * 个人中心查询订购人姓名
     *
     * @return
     */
    @Override
    public Map<String, String> selectUserName() {
        //查询当前
        List<String> userNameList = orderDao.selectUserName();
        List<Map<String, String>> resultList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        int i = 1;
        map.put("key", "0");
        map.put("label", "");
        map.put("value", "全部");
        resultList.add(map);
        for (String s : userNameList) {
            map = new HashMap<>();
            map.put("key", i + "");
            map.put("label", s);
            map.put("value", s);
            i++;
            resultList.add(map);
        }
        return map;
    }


    public static String FormetFileSize(long fileS) {//转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static String FormetFileSize2(long fileS) {//转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public String getycleName(String cycle) {
        String cycleName = CycleType.cycleMap.get(cycle);
        return cycleName;
    }


    public boolean checkValidDay(Date start, Date end, Date check) {
        if ((check.after(start) || check.getTime() == start.getTime()) && (check.before(end) || check.getTime() == end.getTime())) {
            return true;
        } else {
            return false;
        }
    }

}
