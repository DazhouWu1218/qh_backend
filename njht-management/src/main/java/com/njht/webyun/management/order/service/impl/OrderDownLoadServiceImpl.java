package com.njht.webyun.management.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.njht.webyun.management.basis.entity.BasisDataInfo;
import com.njht.webyun.management.business.entity.ProductFileInfo;
import com.njht.webyun.management.business.entity.ProductInfo;
import com.njht.webyun.management.common.constant.Constant;
import com.njht.webyun.management.common.util.DateUtil;
import com.njht.webyun.management.common.util.FileUnpackUtil;
import com.njht.webyun.management.dataanalysis.systemdatacount.entity.LogSystemDataCountEntity;
import com.njht.webyun.management.order.dao.OrderDao;
import com.njht.webyun.management.order.dao.OrderInfoDao;
import com.njht.webyun.management.order.entity.*;
import com.njht.webyun.management.order.service.OrderDownLoadService;
import com.njht.webyun.management.satellite.vo.SatelliteThematicDataFileInfoEntity;
import com.njht.webyun.management.satellite.vo.SatelliteThematicDataInfoEntity;
import com.njht.webyun.management.sys.service.DicService;
import com.njht.webyun.utils.FileNameUtils;
import com.njht.webyun.utils.FileZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author dgj
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class OrderDownLoadServiceImpl implements OrderDownLoadService {

    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderDao orderDao;

    @Value("${downLoadPath}")
    private String downLoadPath;

    @Autowired
    private StringRedisTemplate redisService;


    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
    String format = sdf.format(date);
    String format1 = sdf1.format(date);

    @Override
    @Scheduled(cron = "0 0/1 * * * ?")
    public void getOrderStatus()  {
        // 1.查询所有审核通过数据位准备的订单
        List<OrderInfo> orderInfoList = orderInfoDao.getOrderInfoList();
        if(orderInfoList.isEmpty()){
            return;
        }
        log.info("订单审核通过，数据准备中......");
        orderInfoList.stream().filter(item -> !redisService.hasKey(item.getOrderId())).parallel().forEach(o ->{
            redisService.opsForValue().set(o.getOrderId(),o.getDownloadPath(),1, TimeUnit.MINUTES);
            String username = o.getUsername();
            //订单存放路径 202003/20200329/订单
            String orderPath = "order"+File.separator+username+File.separator+format+File.separator+format1+File.separator+o.getOrderId();

            // 2.改变订单的状态到数据准备中(order_status = 4)
            Integer i = orderInfoDao.updateStatus(o.getId(),4);
            if(i!=0){
                //查找订单的相关数据，并改变订单信息
                if(this.getOrderInfo(o,orderPath,username)){
                    //数据准备成功改变订单的状态 5
                    orderInfoDao.updateStatus(o.getId(),5);
                    log.info("数据准备完成,订单完成");
                }else{
                    orderInfoDao.updateStatus(o.getId(),6);
                    log.info("数据缺失,订单失败");
                }
            }
        });
    }

    @Override
    public Map<String,Object> getOrderIMessage(String userName) {
        Map<String,Object> map = new HashMap<>(16);
        //查询用户相关的信息
        List<OrderMessageDTO> orderMessageList = orderInfoDao.getOrderMessage(userName);
        ArrayList<OrderMessageDTO> collect = orderMessageList.stream().filter(item -> "0".equals(item.getMessageStatus())).collect(Collectors.toCollection(ArrayList::new));
        map.put("size",collect.size());
        map.put("orderMessageList",orderMessageList);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateMessageInfo(List<String> idList,Integer i) {
        idList.forEach(s -> orderInfoDao.updateOrderMessageInfo(s,i));
        return "1";
    }

    /**
     * 查找订单数据并打包，准备下载
     * @param orderInfo
     * @return
     */
    public boolean getOrderInfo(OrderInfo orderInfo,String orderPath,String username){
        //orderInfo 表的userInfo字段存放的username
        boolean flag = false;
        try {
            flag = false;
            List<DownLoad> list = Collections.synchronizedList(new ArrayList<>());
            //通过orderId 拿到该订单有几种类型的
            List<String> orderIdentifier = orderInfoDao.getOrderIdentifier(orderInfo.getOrderId());
            for (String s:orderIdentifier){
                //不同的产品处理方式不同
                if(Constant.BUSINESSPRODUCT.equals(s)){
                    list = downLoadBusinessProduct(s,orderInfo.getOrderId(),orderPath,list,username);
                    if(list == null ||list.isEmpty()){
                        return false;
                    }
                    flag = true;
                }
                //卫星数据
                if(Constant.SATELLITEDATA.equals(s)){
                   list = downLoadSateDataInfo(s,orderInfo.getOrderId(),list,orderPath,username);
                   if(list == null || list.isEmpty()){
                       return false;
                   }
                   flag = true;
                }
                // 卫星专题数据
                if(Constant.SATELLITETHEMATICDATA.equals(s)){
                    list = downLoadSateThematicData(s,orderInfo.getOrderId(),list,orderPath,username);
                    if( list.isEmpty()){
                        return false;
                    }
                    flag = true;
                }
                //基础数据
                if(Constant.BASICDATA.equals(s)){
                    list = downLoadBasicDataInfo(s,orderInfo.getOrderId(),list,orderPath,username);
                    if(list.isEmpty()){
                        return false;
                    }
                    flag = true;
                }
                //专题数据
                if(Constant.THEMATICDATA.equals(s)){
                    list = downLoadThematicInfo(s,orderInfo.getOrderId(),list,orderPath,username);
                    if (list.isEmpty()){
                        return  false;
                    }
                    flag = true;
                }
            }
            if(!list.isEmpty()){
                Integer i = orderInfoDao.updateDownLoadPath(orderInfo.getOrderId(), JSON.toJSONString(list).replace("'",""));
                if(i == 0){
                    flag = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return flag;
    }

    /**
     * 卫星专题数据下载
     * @param s
     * @param orderId
     * @param list
     * @param orderPath
     * @param userName
     * @return
     */
    private List<DownLoad> downLoadSateThematicData(String s, String orderId, List<DownLoad> list, String orderPath, String userName) {
        //根据订单id查询数购物车中的dataId以及 dataType
        List<OrderListInfo> orderListInfo = orderInfoDao.getOrderListInfo(orderId, s);
        //该订单id中对应的数据
        ArrayList<String> collect = orderListInfo.stream().map(OrderListInfo::getDataId).collect(Collectors.toCollection(ArrayList::new));
        //查詢到订单中相关的卫星遥感专题数据
        List<SatelliteThematicDataInfoEntity> satelliteThematicDataInfoList = orderInfoDao.getSatelliteThematicDataInfo(collect);
        //每一种数据放到一个压缩包内下载
        satelliteThematicDataInfoList.forEach(item -> {
            String fileName = "卫星专题数据"+item.getMark()+"_"+item.getCycle()+"_"+item.getIssue()+".zip";
            ArrayList<OrderListInfo> orderInfoList = orderListInfo.stream().filter(item1 -> item1.getDataId().equals(item.getId())).collect(Collectors.toCollection(ArrayList::new));
            //需要下载的文件集合
            List<File> fileList = new ArrayList<>();
            String dataTypes = orderInfoList.stream().map(OrderListInfo::getDataType).collect(Collectors.joining(","));
            if(dataTypes.contains("0")){
                //原始数据路径
                String originalPath = item.getFilePath();
                String[] split = originalPath.split(",");
                List<String> originalList = Arrays.asList(split);
                originalList.forEach(s1 -> fileList.add(new File(s1)));
            }
            if(dataTypes.contains("1")){
                List<SatelliteThematicDataFileInfoEntity> dateFileInfoList = item.getDateFileInfoList();
                dateFileInfoList.forEach(s2 -> fileList.add(new File(s2.getFilePath())));
            }
            DownLoad downLoad = getDownLoadInfo(fileList,downLoadPath+File.separator+orderPath,fileName,userName,orderId);
            list.add(downLoad);
            //下载的文件做统计
            insertOrderFileInfo(downLoadPath+File.separator+orderPath,fileName,null,null);
        });
        return list;
    }

    /**
     * 专题数据下载
     * @param s 订单标识
     * @param orderId
     * @param list
     * @param orderPath
     * @param userName
     * @return
     */
    private List<DownLoad> downLoadThematicInfo(String s, String orderId, List<DownLoad> list, String orderPath, String userName) {
        List<OrderListInfo> infos = orderInfoDao.getOrderListInfo(orderId,s);
        infos.forEach(item -> {
            String[] times = item.getTime().split("-");
            String beginTime = times[0].replace(".","");
            String endTime = times[1].replace(".","");
            List<String> pathList  = orderInfoDao.getThematicDataInfo(item.getDataId(),beginTime,endTime);
            List<File> fileList =  Collections.synchronizedList(new ArrayList<>());
            pathList.forEach(s1 -> fileList.add(new File(s1)));
            String fileName = item.getTime()+fileList.get(0).getParentFile().getName()+".zip";
            DownLoad downLoad = getDownLoadInfo(fileList,downLoadPath+File.separator+orderPath,fileName,userName,orderId);
            list.add(downLoad);
            String dataName = orderInfoDao.getThematicInfo(item.getDataId());
            String dataType = "专题数据";
            insertOrderFileInfo(downLoadPath+File.separator+orderPath,fileName,dataName,dataType);
        });
        return list;
    }

    /**
     * 基础数据下载
     * @param s
     * @param orderId
     * @param list
     * @param orderPath
     * @return
     */
    private List<DownLoad> downLoadBasicDataInfo(String s, String orderId, List<DownLoad> list, String orderPath,String userName) {
        //获取订单中的所有基础数据信息
        List<BasisDataInfo> infoList = orderInfoDao.getBasicDataList(orderId,s);
        for(BasisDataInfo info:infoList){
            String path = info.getFilePath();
            if(path == null ){
                log.info("没有"+info.getName()+"相关的文件信息");
                continue;
            }
            File file = new File(path);
            String name = info.getName();
            try {
                FileZipUtils.zipFiles(file,downLoadPath+File.separator+orderPath,name+".zip");
                DownLoad downLoad = new DownLoad();
                downLoad.setName(name);
                String url = downLoadUrl(userName);
                downLoad.setUrl((url+File.separator+orderId+File.separator+name+".zip").replace("\\","/"));
                list.add(downLoad);
                // 开启另一个线程 添加到订单中的文件信息入库
                insertOrderFileInfo(downLoadPath+File.separator+orderPath,name+".zip",null,null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 业务产品下载
     * @param s
     * @param orderId
     * @param orderPath
     * @param list
     * @return
     */
    private List<DownLoad> downLoadBusinessProduct(String s, String orderId, String orderPath, List<DownLoad> list,String username) {
        //获取产品id
        List<ProductInfo> productInfos =  orderInfoDao.getProductInfoId(orderId,s);
        for (ProductInfo p:productInfos){
            List<ProductFileInfo> productFileInfos = orderInfoDao.getProductInfoList(p.getId());
            if(!productFileInfos.isEmpty()){
                //原文件路径
                String filePath = productFileInfos.get(0).getFilePath();
                String relativePath = productFileInfos.get(0).getRelativePath();
                File file= new File(filePath);
                log.info("业务产品数据路径：{}",file.getPath());
                File parentFile = file.getParentFile();
                if(!parentFile.exists()){
                    log.info("没有业务产品数据"+parentFile+"---该路径下没有数据");
                    return Collections.emptyList();
                }
                //文件的压缩目录
                String basePath = filePath.substring(0,filePath.indexOf(relativePath));
                String dirName = p.getMark()+"_"+p.getIssue()+".zip";
                try {
                    log.info("业务产品开始压缩...");
                    FileZipUtils.zipFiles(parentFile,basePath+File.separator+ orderPath,dirName);
                    DownLoad downLoad = new DownLoad();
                    String name = p.getName()+"_"+p.getIssue();
                    downLoad.setName(name);
                    String url = downLoadUrl(username);
                    downLoad.setUrl((url+File.separator+orderId+File.separator+dirName).replace("\\","/"));
                    list.add(downLoad);
                    //根据id查询到所属分类产品名称
                    String dataName = orderInfoDao.getBusinessProductName(p.getId());
                    String dataType = "业务产品";
                    insertOrderFileInfo(basePath+File.separator+ orderPath,dirName,dataName,dataType);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                    return Collections.emptyList();
                }
            }
        }
        return list;
    }

    /**
     * 卫星数据下载
     * @param s
     * @param orderId
     * @param list
     * @param orderPath
     * @return
     */
    private List<DownLoad> downLoadSateDataInfo(String s, String orderId,List<DownLoad> list,String orderPath,String userName) {
        //根据添加到购物车的数据id 查找到卫星数据的相关信息
        List<SateDataOrderEntity> filePathList =  orderInfoDao.getSateDateInfo(orderId,s);
        if(filePathList.isEmpty()){
            log.info("没有符合条件的卫星数据");
            return Collections.emptyList();
        }
        //根据id分组，每个id下载一个压缩包
        Map<String, List<SateDataOrderEntity>> collect = filePathList.stream().collect(Collectors.groupingBy(SateDataOrderEntity::getId));
        collect.forEach((key, value1) -> {
            //H8
            if ("H8".equals(value1.get(0).getSatellite())) {
                DownLoad h8Info = downLoadH8Info(orderPath, value1, userName, orderId);
                list.add(h8Info);
            } else {
                List<DownLoad> originalDownLoadList = downLoadOriginalSateInfo(orderPath, value1, userName, orderId);
                if (!originalDownLoadList.isEmpty()) {
                    list.addAll(originalDownLoadList);
                }
            }
        });
        return list;
    }

    /**
     * 高低分辨率卫星数据下载
     * @param dirPath
     * @param value
     * @param userName
     * @param orderId
     * @return
     */
    private List<DownLoad> downLoadOriginalSateInfo(String dirPath, List<SateDataOrderEntity> value, String userName, String orderId) {
        List<DownLoad> list = new ArrayList<>();
        SateDataOrderEntity s1 = value.get(0);
        String dataType1 = value.get(0).getDatatype();
        File file = new File(s1.getOriginalPath().replace("\\","/"));
        if(!file.exists()){
            log.info(s1+"原始数据暂时没有上传");
            return Collections.emptyList();
        }
        String name = file.getName();
        String toPath = downLoadPath+File.separator+dirPath;
        String regex = "(^.*)(\\.zip$|\\.rar$|(?<!\\.tar)\\.gz$|\\.tar\\.gz$)";
        Pattern zipPattern = Pattern.compile(regex);
        Matcher matcher = zipPattern.matcher(file.getName());
        //已经压缩好的直接下载
        DownLoad downLoad = new DownLoad();
        String dataType = "";

        if(matcher.find()){
            if(dataType1.contains("1")){
                log.info("复制文件"+s1+"从原始路径到ftp目录");
                FileUnpackUtil.copy(s1.getOriginalPath(),toPath.replace("\\","/")+File.separator+name);
                log.info(s1+"该文件复制结束");
                downLoad.setName(name.substring(0,name.lastIndexOf(".")));
                String url = downLoadUrl(userName);
                downLoad.setUrl((url+File.separator+orderId+File.separator+name).replace("\\","/"));
                dataType = "源数据";
                String dataName = value.get(0).getSatellite();
                insertOrderFileInfo(downLoadPath+File.separator+dirPath,name,dataName,dataType);
                list.add(downLoad);
            }
            if(dataType1.contains("0")){
                log.info("高分暂不支持预处理数据下载");
            }
        }else{
            //没有压缩的压缩下载
            log.info("------------->低分辨率卫星数据下载-------------");
            List<File> fileList = new ArrayList<>();
            if(dataType1.contains("0")){
                //添加tif文件
                fileList.add(new File(s1.getTifPath()));
            }
            if(dataType1.contains("1")){
                //添加原始文件
                fileList.add(file);
            }
            String downLoadName = FileNameUtils.getFileRealName(file)+".zip";
            downLoad = getDownLoadInfo(fileList, toPath, downLoadName, userName, orderId);
            dataType = "预处理数据";
            String dataName = value.get(0).getSatellite();
            insertOrderFileInfo(downLoadPath+File.separator+dirPath,name,dataName,dataType);
            list.add(downLoad);
        }

        return list;
    }

    /**
     * h8 卫星数据下载
     * @param dirPath
     * @param value
     * @param userName
     * @param orderId
     * @return
     */
    private DownLoad downLoadH8Info(String dirPath, List<SateDataOrderEntity> value, String userName, String orderId) {
        //下载H8对应的tif信息
        log.info("---------------> 下载H8对应的数据");
        ArrayList<File> collect =
                value.stream().filter(item -> item.getTifPath() != null && !"".equals(item.getTifPath()))
                        .map(item -> new File(item.getTifPath())).collect(Collectors.toCollection(ArrayList::new));
        String regex = "H08_(\\d{8})_(\\d{4})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(collect.get(0).getName());
        String name = null;
        if(matcher.find()){
            name = matcher.group();
        }else{
            name = "H8";
        }
        String toPath = downLoadPath+File.separator+dirPath;
        String downLoadName = name + ".zip";
        DownLoad downLoad = getDownLoadInfo(collect, toPath, downLoadName, userName, orderId);
        //下载信息入库
        String dataName = "H8";
        String dataType = "预处理数据";
        insertOrderFileInfo(downLoadPath+File.separator+dirPath,downLoadName,dataName,dataType);
        return downLoad;
    }


    @Autowired
    private DicService dicService;
    /**
     * 根据用户信息拼接下载url
     * @param userName
     * @return
     */
    public String downLoadUrl(String userName){
        String downUrl = dicService.selectDownUrl();
        return downUrl+File.separator+userName+File.separator+format+ File.separator+format1;
    }

    /**
     * 下载文件信息入库
     * @param dirPath
     * @param dirName
     */
    public void insertOrderFileInfo(String dirPath,String dirName,String dataName,String dataType){
        Runnable runnable = () -> {
            String filePath = dirPath +File.separator+dirName;
            //判断文件是够已经入库，没入库的再入库
            OrderFileInfo orderFileInfo = orderDao.selectOrderFile(filePath);
            if(orderFileInfo != null){
                return;
            }
            Integer categoryId = null;
            //根据dataName 以及 dataType 查找所属数据类型
            if(dataName != null && dataType != null){
                categoryId = orderInfoDao.getSysDataCategoryId(dataName,dataType);
            }
            File file = new File(filePath);
            String id = UUID.randomUUID().toString().replace("-","");
            String fileName = file.getName();
            long length = file.length();
            String fileSize = String.valueOf(length);
            try {
                orderDao.insertOrderFileInfo(new OrderFileInfo(id,date,date,filePath.replace("\\","/"),fileName,fileSize,categoryId));
                //根据文件大小以及所属文件类型对统计结果入库
                updateDataCount(categoryId,fileSize);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                log.info(filePath+"-->文件入库失败");
            }
        };
        new Thread(runnable).start();
    }

    private void updateDataCount(Integer categoryId, String fileSize) {
        try {
            String dateToStr = DateUtil.dateToStr(new Date(), DateUtil.formatYY_MM_dd1);
            //查询有没有当天的数据
            LogSystemDataCountEntity countEntity = orderInfoDao.selectDataInfo(categoryId,dateToStr);
            if(countEntity == null){
                orderInfoDao.insertDataInfo(new LogSystemDataCountEntity(Long.valueOf(categoryId),new Date(),Long.valueOf(fileSize)));
            }else{
                Long dataSize = Long.valueOf(fileSize)+countEntity.getDataSize();
                orderInfoDao.updateDataInfo(countEntity.getId(),dataSize);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 把文件压缩到指定目录，并返回对象
     * @param fileList
     * @param filePath
     * @param fileName
     * @param userName
     * @param orderId
     * @return
     */
    private DownLoad getDownLoadInfo(List<File> fileList, String filePath, String fileName,String userName,String orderId) {
        FileZipUtils.zipFiles(fileList,filePath,fileName);
        DownLoad downLoad = new DownLoad();
        downLoad.setName(fileName);
        String url = downLoadUrl(userName);
        downLoad.setUrl((url+File.separator+orderId+File.separator+fileName).replace("\\","/"));
        return downLoad;
    }

}
