package com.njht.webyun.management.upload.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.management.business.service.BusinessProductService;
import com.njht.webyun.management.common.util.DateUtil;
import com.njht.webyun.management.common.util.RedisService;
import com.njht.webyun.management.dataanalysis.systemdatacount.utils.FileCountUtils;
import com.njht.webyun.management.upload.dao.DataAnalysisDao;
import com.njht.webyun.management.upload.dto.BusinessStatisticDTO;
import com.njht.webyun.management.upload.dto.DiskCapacityDTO;
import com.njht.webyun.management.upload.dto.FileInfoDTO;
import com.njht.webyun.management.upload.entity.BusinessStatisticEntity;
import com.njht.webyun.management.upload.service.DataAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 代国军
 * @CreateDate: 2021/6/23 10:43
 * @Description: 数据管理统计分析实现类
 */
@Slf4j
@Service("dataAnalysisService")
public class DataAnalysisServiceImpl implements DataAnalysisService {


    /**
     * 统计系统磁盘容量 缓存方法RedisKey
     */
    public static final String SYSTEM_LOG_DISK_CAPACITY_METHOD_CACHE = "systemLog:DiskCapacity:method:cache";

    @Value("${dataAnalysis.disk}")
    private String disk;

    @Autowired
    private DataAnalysisDao dataAnalysisDao;

    @Autowired
    private BusinessProductService businessProductService;

    @Autowired
    private RedisService redisService;

    public static final String WEEK = "week";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String TIF = "tif";
    public static final String DOC = "doc";
    public static final String DOCX = "docx";
    public static final String XLS = "xls";
    public static final String XLSX = "xlsx";
    public static final String JPG = "jpg";
    public static final String redisKey = "a8a6c2fbbf9e4d84bfa9d4a2a11cc66f";
    public static final Long ONE_DAY = 24 * 3600L;

    @Override
    @Cacheable(value = SYSTEM_LOG_DISK_CAPACITY_METHOD_CACHE)
    public DiskCapacityDTO getDiskCapacity() {
        // 当前文件系统类
        FileSystemView fsv = FileSystemView.getFileSystemView();
        // 列出所有windows 磁盘
        File[] fs = Optional.ofNullable(File.listRoots()).orElseGet(() -> new File[0]);
        log.info("windows中有{}个磁盘", fs.length);
        List<File> viewList = Arrays.asList(fs);
        ArrayList<File> collect = viewList.stream().filter(file -> fsv.getSystemDisplayName(file).contains(disk)).collect(Collectors.toCollection(ArrayList::new));
        DiskCapacityDTO diskCapacityDTO = new DiskCapacityDTO();
        if (CollectionUtils.isNotEmpty(collect)) {
            diskCapacityDTO.setTotalSpace(FileCountUtils.byteToPrettyUnit(collect.get(0).getTotalSpace()));
            diskCapacityDTO.setFreeSpace(FileCountUtils.byteToPrettyUnit(collect.get(0).getFreeSpace()));
        } else {
            log.error("统计失败!!!");
        }
        return diskCapacityDTO;
    }

    /**
     * 获取业务生产信息
     * 带缓存的方法
     *
     * @return
     */
    @Override
    public List<BusinessStatisticDTO> getBusinessStatisticInfo() {
        if (redisService.exists(redisKey)) {
            String o = (String) redisService.get(redisKey);
            return JSON.parseArray(o, BusinessStatisticDTO.class);
        } else {
            return getBusinessStatisticEntityList();
        }
    }


    /**
     * 缓存方法的redisKey前缀
     * 用作缓存所有业务产品的文件大小累加
     */
    public static final String DATA_ANALYSIS_SERVICE_ALL_FILE_COUNT = "systemLog:serviceProduct:method:allFileCount";

    /**
     * 业务产品生产模块统计分析 将所有文件大小累加
     *
     * @return
     */
    @Override
    @Cacheable(value = DATA_ANALYSIS_SERVICE_ALL_FILE_COUNT)
    public Long countAllBusinessStatisticInfo() {
        log.info("统计业务生产所有文件大小");
        final Long countByte = dataAnalysisDao.countAllBusinessStatisticInfo();
        return countByte;
    }


    @Override
    @Scheduled(cron = "0 0 23 * * ?")
    public List<BusinessStatisticDTO> getBusinessStatisticEntityList() {
        //查询业务产品专题图，栅格，word，xls等结果
        List<BusinessStatisticEntity> businessStatisticEntityList = dataAnalysisDao.getBusinessStatisticInfo();

        List<BusinessStatisticDTO> resultList = new ArrayList<>();
        //周统计结果
        BusinessStatisticDTO weekDTO = getFileInfoList(businessStatisticEntityList, WEEK);
        resultList.add(weekDTO);
        //月统计结果
        BusinessStatisticDTO monthDTO = getFileInfoList(businessStatisticEntityList, MONTH);
        resultList.add(monthDTO);
        //年统计结果
        BusinessStatisticDTO yearDTO = getFileInfoList(businessStatisticEntityList, YEAR);
        resultList.add(yearDTO);

        //拿redis缓存统计分析的结果并添加定时任务实时的更新数据
        boolean b = redisService.exists(redisKey);
        if (b) {
            redisService.remove(redisKey);
        }
        redisService.set(redisKey, JSONObject.toJSONString(resultList), ONE_DAY);
        return resultList;
    }

    /**
     * 缓存方法的redisKey前缀
     * 用作缓存所有业务产品的文件大小累加
     */
    public static final String DATA_ANALYSIS_PRETREATMENT_ALL_FILE_COUNT = "systemLog:pretreatment:method:allFileCount";


    /**
     * 获取预处理数据总量
     *
     * @return
     */
    @Cacheable(value = DATA_ANALYSIS_PRETREATMENT_ALL_FILE_COUNT)
    @Override
    public Long getPretreatmentProduct() {
        /* 获取 cimiss_grib_data_file_info dataType=0的文件统计 */
        // 格点数据
        return null;
    }

    /**
     * 获取统计分析结果（按周，按月，按年）
     *
     * @param businessStatisticList
     * @param identify
     * @return
     */
    private BusinessStatisticDTO getFileInfoList(List<BusinessStatisticEntity> businessStatisticList, String identify) {
        //根据时间过滤
        List<String> dateList = getDate(identify);
        ArrayList<BusinessStatisticEntity> businessStatisticEntityList = businessStatisticList.stream().filter(item -> {
            int begin = item.getIssue().compareTo(dateList.get(1));
            int end = item.getIssue().compareTo(dateList.get(0));
            if (begin >= 0 && end <= 0) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
        BusinessStatisticDTO businessStatisticDTO = new BusinessStatisticDTO();
        businessStatisticDTO.setTimeRange(identify);
        List<FileInfoDTO> fileInfoDTOList = new ArrayList<>();

        //根据id分组
        Map<String, List<BusinessStatisticEntity>> map = businessStatisticEntityList.stream()
                .filter(businessStatisticEntity -> businessStatisticEntity.getTreeId() != null)
                .collect(Collectors.groupingBy(BusinessStatisticEntity::getTreeId));
        //获取到不同数据所属分类的信息
        List<Tree> productTreeList = businessProductService.findProductTree();
        Map<String, List<String>> idMap = new HashMap<>(16);
        getChildrenList(productTreeList, idMap);
        Map<String, List<BusinessStatisticEntity>> nameMap = new HashMap<>(16);
        //根据id 把每种数据存放到对应的数据类型下
        idMap.forEach((s, strings) -> {
            List<BusinessStatisticEntity> list = new ArrayList<>();
            strings.forEach(s1 -> {
                List<BusinessStatisticEntity> childrenBusiness = map.get(s1);
                if (childrenBusiness != null) {
                    list.addAll(childrenBusiness);
                }
            });
            nameMap.put(s, list);
        });
        nameMap.forEach((key, value) -> {
            FileInfoDTO fileInfoDTO = new FileInfoDTO();
            fileInfoDTO.setProductName(key);
            Map<String, List<BusinessStatisticEntity>> collect = value.stream().collect(Collectors.groupingBy(BusinessStatisticEntity::getFileType));
            collect.forEach((s, item) -> {
                if (s.equals(TIF)) {
                    long tifSum = item.stream().mapToLong(value1 -> Long.parseLong(value1.getFileSize())).sum();
                    fileInfoDTO.setTifSize(this.getFileSizeToMB(tifSum));
                } else if (s.equals(DOC) || s.equals(DOCX)) {
                    long docSum = item.stream().mapToLong(value1 -> Long.parseLong(value1.getFileSize())).sum();
                    fileInfoDTO.setDocSize(this.getFileSizeToMB(docSum));
                } else if (s.equals(XLS) || s.equals(XLSX)) {
                    long xlsSum = item.stream().mapToLong(value1 -> Long.parseLong(value1.getFileSize())).sum();
                    fileInfoDTO.setXlsSize(this.getFileSizeToMB(xlsSum));
                } else if (s.equals(JPG)) {
                    long jpgSum = item.stream().mapToLong(value1 -> Long.parseLong(value1.getFileSize())).sum();
                    fileInfoDTO.setJpgSize(this.getFileSizeToMB(jpgSum));
                } else {
                }
            });
            fileInfoDTOList.add(fileInfoDTO);
        });
        businessStatisticDTO.setUnit("MB");
        businessStatisticDTO.setFileList(fileInfoDTOList);
        return businessStatisticDTO;
    }

    private String getFileSizeToMB(long size) {
        DecimalFormat format = new DecimalFormat("###.00");
        double i = (size / (1024.0 * 1024.0));
        return format.format(i);
    }

    /**
     * 获取子集的id
     *
     * @param treeNodes
     * @param idMap
     * @return
     */
    public static Map<String, List<String>> getChildrenList(List<Tree> treeNodes, Map<String, List<String>> idMap) {
        for (Tree treeNode : treeNodes) {
            if ("0".equals(treeNode.getParentId())) {
                idMap.put(treeNode.getLabel(), findChildren(treeNode, treeNodes, new ArrayList<>()));
            }
        }
        return idMap;
    }

    public static List<String> findChildren(Tree treeNode, List<Tree> treeNodes, List<String> list) {
        for (Tree it : treeNodes) {
            if (treeNode.getValue().equals(it.getParentId())) {
                if (treeNode.getChildren() == null) {
                    list.add(it.getValue());
                }
                findChildren(it, treeNodes, list);
            }
        }
        return list;
    }

    /**
     * 获取当前时间 往前一个月|当前时间前一周|当前时间前一年的时间字符串（yyyyMMddHHmmss）
     *
     * @return
     */
    public List<String> getDate(String identify) {
        List<String> list = new ArrayList<>();
        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        String endTime = DateUtil.dateToStr(calendar.getTime(), DateUtil.formatYYMMddHHmmss);
        String beginTime = "";
        list.add(endTime);
        if (WEEK.equals(identify)) {
            calendar.add(Calendar.WEEK_OF_MONTH, -1);
            beginTime = DateUtil.dateToStr(calendar.getTime(), DateUtil.formatYYMMddHHmmss);
        } else if (MONTH.equals(identify)) {
            calendar.add(Calendar.MONTH, -1);
            beginTime = DateUtil.dateToStr(calendar.getTime(), DateUtil.formatYYMMddHHmmss);
        } else if (YEAR.equals(identify)) {
            calendar.add(Calendar.YEAR, -1);
            beginTime = DateUtil.dateToStr(calendar.getTime(), DateUtil.formatYYMMddHHmmss);
        } else {
            beginTime = endTime;
        }
        list.add(beginTime);
        return list;
    }

}
