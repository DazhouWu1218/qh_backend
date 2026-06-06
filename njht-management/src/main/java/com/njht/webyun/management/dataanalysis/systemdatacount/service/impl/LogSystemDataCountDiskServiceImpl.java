package com.njht.webyun.management.dataanalysis.systemdatacount.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.njht.webyun.management.dataanalysis.systemdatacount.dao.LogSystemDataCategoryDao;
import com.njht.webyun.management.dataanalysis.systemdatacount.dto.LogSystemDataCategoryDTO;
import com.njht.webyun.management.dataanalysis.systemdatacount.entity.LogSystemDataCategoryEntity;
import com.njht.webyun.management.dataanalysis.systemdatacount.service.LogSystemDataCountDiskService;
import com.njht.webyun.management.dataanalysis.systemdatacount.service.LogSystemDataCountService;
import com.njht.webyun.management.dataanalysis.systemdatacount.utils.FileOperationUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 计算磁盘上的数据量Service
 *
 * @author zhouhouliang
 * @date 2021/7/14 15:02
 */
@Slf4j
@Service
@Transactional
public class LogSystemDataCountDiskServiceImpl implements LogSystemDataCountDiskService {

    /**
     * 统计近几天的
     */
    @Value("${system.datacount.backendDays:1}")
    private Integer backendDays;

    /**
     * 数据分类Service
     */
    @Autowired
    public LogSystemDataCategoryDao logSystemDataCategoryDao;

    /**
     * 数据统计Service
     */
    @Autowired
    LogSystemDataCountService logSystemDataCountService;


    @Autowired
    public ObjectMapper objectMapper;

    /**
     * 计算数据接收
     */
    @Override
    @SneakyThrows
    public void calculateDataReceive() {
        final long currentTimeMillis = System.currentTimeMillis();
        log.info(">>>>>>>>>> 开始计算各类数据的接收量 >>>>>>>>>>");
        final List<LogSystemDataCategoryEntity> logSystemDataCategoryEntities = logSystemDataCategoryDao.selectByAssertDownload(false);
        // 获取所有接收的数据类型
        final List<LogSystemDataCategoryDTO> logSystemDataCategoryDTOS = logSystemDataCategoryEntities.stream().map(logSystemDataCategoryEntity -> {
            final LogSystemDataCategoryDTO logSystemDataCategoryDTO = new LogSystemDataCategoryDTO();
            BeanUtils.copyProperties(logSystemDataCategoryEntity, logSystemDataCategoryDTO);
            final String[] pathArray = Optional.ofNullable(logSystemDataCategoryEntity.getPath()).orElseGet(String::new).split(",");
            logSystemDataCategoryDTO.setPathList(Arrays.asList(pathArray));
            final String matchRegexJson = logSystemDataCategoryEntity.getMatchRegexJson();
            if (StringUtils.isNotBlank(matchRegexJson)) {
                final Map<String, String> matchRegexMap = readJsonValue(matchRegexJson, LinkedHashMap.class);
                logSystemDataCategoryDTO.setMatchRegexMap(matchRegexMap);
            }
            return logSystemDataCategoryDTO;
        }).filter(logSystemDataCategoryDTO -> StringUtils.isNotBlank(logSystemDataCategoryDTO.getPath())).collect(Collectors.toList());
        log.info("获取到{}种数据类型", logSystemDataCategoryDTOS.size());


        /* ---- 统计最近几天(0-n)天的数据接收量 ---- */
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -backendDays);
        // 有效的统计天,在此天之前,概不统计
        String validDateFormat = DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd");
        Date validDayDate = DateUtils.parseDate(validDateFormat, "yyyy-MM-dd");
        log.info("统计[{}]之后的数据", validDateFormat);


        // 初始化一个Map,统计各类型数据  泛型说明: key: 分类   value: Map<时间,数据量>
        final ConcurrentHashMap<LogSystemDataCategoryDTO, HashMap<String, Long>> allCategoryCountMap = new ConcurrentHashMap<>();
        logSystemDataCategoryDTOS.parallelStream().forEach(logSystemDataCategoryDTO -> {
            // 计算一个分类的数据量
            calculateEachCategory(allCategoryCountMap, logSystemDataCategoryDTO, validDayDate);
        });
        // 统计完成后入库
        allCategoryCountMap.forEach((logSystemDataCategoryDTO, dateCountMap) -> {
            dateCountMap.forEach((date, count) -> {
                try {
                    logSystemDataCountService.updateOrInsertSystemDataCount(false, logSystemDataCategoryDTO.getId(), DateUtils.parseDate(date, "yyyyMMdd"), count);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        });
        log.info(">>>>>>>>>> 完成计算各类数据的接收量,耗时:{} >>>>>>>>>>", System.currentTimeMillis() - currentTimeMillis);
    }


    /**
     * 计算数据的下载
     */
    @Override
    public void calculateDataDownload() {
        log.info(">>>>>>>>>> 开始计算各类数据的分发量 >>>>>>>>>>");
        final long start = System.currentTimeMillis();
        /* ---------- 查找数据下载表 ---------- */
        // order_file_info 里有订单文件夹下载信息


        //

        log.info(">>>>>>>>>> 完成计算各类数据的接收量,耗时:{} >>>>>>>>>>", System.currentTimeMillis() - start);
    }

    /**
     * 计算某一个分类的数据
     *
     * @param allCategoryDateCountMap  汇总map
     * @param logSystemDataCategoryDTO 单个分类
     */
    private void calculateEachCategory(ConcurrentHashMap<LogSystemDataCategoryDTO, HashMap<String, Long>> allCategoryDateCountMap,
                                       LogSystemDataCategoryDTO logSystemDataCategoryDTO, Date validDayDate) {
        final List<String> pathList = logSystemDataCategoryDTO.getPathList().stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        log.info("分类[{}-{}]下有{}个路径匹配", logSystemDataCategoryDTO.getDataType(), logSystemDataCategoryDTO.getDataName(), pathList.size());
        if (CollectionUtils.isNotEmpty(pathList)) {
            // 统计所有的 pathList 指定的文件夹
            final List<Map<String, Long>> allPathDateSizeMapList = pathList.parallelStream().map(matchString -> {
                // 统计单个文件夹  Map<日期,数据量>
                final Map<String, Long> dateSizeMap = countPathPattern(matchString, validDayDate);
                return dateSizeMap;
            }).collect(Collectors.toList());
            // 将所有文件夹下的数据统计,按相同日期合并计算
            // List<Map<String,Long>> -> Map<String,Long>
            final HashMap<String, Long> dateCountSizeMap = new HashMap<>();
            for (Map<String, Long> singlePathDateSize : allPathDateSizeMapList) {
                singlePathDateSize.forEach((date, size) -> {
                    final Long count = Optional.ofNullable(dateCountSizeMap.get(date)).orElse(0L);
                    dateCountSizeMap.put(date, count + size);
                });
            }
            allCategoryDateCountMap.put(logSystemDataCategoryDTO, dateCountSizeMap);
        }
    }


    /**
     * 统计某一个路径匹配字符匹配到的文件夹
     *
     * @param matchString
     * @return Map<日期, 容量>
     */
    private Map<String, Long> countPathPattern(String matchString, Date validDayDate) {
        log.info("执行[{}]的寻找方法...", matchString);
        long time1 = System.currentTimeMillis();
        List<File> patternMatchedFiles = findPatternMatchedFiles(matchString, validDayDate);
        log.info("======================== matchString:[{}] 匹配到{}个文件 ,耗时:{}ms ======================== ",
                matchString, patternMatchedFiles.size(),System.currentTimeMillis()-time1);
        time1 = System.currentTimeMillis();
        final ConcurrentHashMap<String, Long> dateSizeMap = new ConcurrentHashMap<>();
        // 统计文件大小
        patternMatchedFiles.parallelStream().forEach(file -> {
            // 按天统计file下的数据量
            final Map<String, Long> fileSizeGroupByDay = FileOperationUtil.getFileSizeGroupByDay(file, validDayDate);
            // 汇总到 dateSizeMap
            mergeItemMapToBigMap(fileSizeGroupByDay, dateSizeMap);
        });
        log.info("======================== 完成分量统计:matchString:[{}] 匹配到{}个文件,耗时:{} ======================== ",
                matchString, patternMatchedFiles.size(),System.currentTimeMillis()-time1);
        return dateSizeMap;
    }


    /**
     * 匹配时间表达式
     */
    private Pattern timePattern = Pattern.compile("\\{yyyy(MM)?(dd)?\\}");


    /**
     * 解析路径匹配字符匹配的Directory,返回路径表达式匹配的字段
     * <p>
     * 寻找路径的时间过滤，只会按照文件名进行过滤。(文件夹的创建,修改时间都不可信)
     * <p>
     * 路径匹配字符样例:
     * Y:\download\H8
     * Y:\download\FY*\{yyyyMMdd}
     * Y:\download\H8\{yyyyMMdd}
     * W:\PUUSDATA\{yyyyMM}\MERSI
     * E:\数据入库-样例数据\*\*\H8\HA*
     *
     * @param fileMatchPattern
     * @param validDayDate
     * @return
     */
    private List<File> findPatternMatchedFiles(String fileMatchPattern, Date validDayDate) {
        fileMatchPattern = fileMatchPattern.replace("\\", "/");
        final List<File> matchedFiles = Collections.synchronizedList(new ArrayList<File>());
        /* ------------ 解析匹配字段 ------------ */
        // 匹配字段样例: Y:\download\FY*\{yyyyMMdd}  W:\PUUSDATA\{yyyyMM}\MERSI  Y:\download\H8\{yyyyMMdd}
        /* 路径是否被模糊表达式隔断 */
        if (fileMatchPattern.contains("*") || timePattern.matcher(fileMatchPattern).find()) {
            // 路径里包含模糊表达式
            // 按照模糊表达式隔断路径, 例如: Y:/download/FY*/{yyyyMMdd} --> ["Y:/download/","FY*","{yyyyMMdd}"]
            String[] pathSplits = StringUtils.split(fileMatchPattern, "/");
            // 将拆分开的路径,进行向下一级一级匹配,将最终的结果汇总到集合
            final File rootFile = new File(pathSplits[0]);
            findPatternMatchedDirectory(rootFile, 1, pathSplits, matchedFiles, validDayDate);
        } else {
            // 路径里不包含模糊表达式
            File file = new File(fileMatchPattern);
            if (file.exists()) {
                matchedFiles.add(file);
            }
        }
        return matchedFiles;
    }


    /**
     * itemMap  汇总到 bigMap
     *
     * @param itemMap
     * @param bigMap
     */
    private static void mergeItemMapToBigMap(Map<String, Long> itemMap, Map<String, Long> bigMap) {
        itemMap.forEach((date, size) -> {
            final Long count = Optional.ofNullable(bigMap.get(date)).orElse(0L);
            bigMap.put(date, count + size);
        });
    }

    /**
     * 递归寻找包含要找的文件的文件夹
     *
     * @param parentDirectoryFile
     * @param level               当前递归的深度
     * @param pathSplits
     * @param matchedFiles
     */
    private void findPatternMatchedDirectory(File parentDirectoryFile, int level, String[] pathSplits, final List<File> matchedFiles, Date validDayDate) {
        int pathSplitsLastIndex = pathSplits.length - 1;
        final String fileNameMatchPattern = pathSplits[level];
        File[] brotherDirectories = Optional.ofNullable(parentDirectoryFile.listFiles()).orElseGet(() -> new File[0]);
        final List<File> validBrotherDirectories = Arrays.stream(brotherDirectories).
                filter(file -> checkFileNameMatch(file.getName(), fileNameMatchPattern, validDayDate))
                .collect(Collectors.toList());
        if (level < pathSplitsLastIndex) {
            // 未到达最底层
            validBrotherDirectories.forEach(file -> {
                findPatternMatchedDirectory(file, level + 1, pathSplits, matchedFiles, validDayDate);
            });
        } else {
            // 到达最底层
            matchedFiles.addAll(validBrotherDirectories);
        }
    }

    /**
     * 判断匹配字符是否和文件名 匹配
     * 例如: GF* 匹配 GF1 GF2ABC
     *
     * @param fileName
     * @param fileNameMatchPattern
     * @return
     */
    private boolean checkFileNameMatch(String fileName, String fileNameMatchPattern, Date validDate) {
        /* ------------------------ 自定义的匹配字符转换为正则表达式 ------------------------ */
        String regex = parsePathPatternToRegex(fileNameMatchPattern);
        /* ------------------------ 判断是否合规 ------------------------ */
        if (!fileNameMatchPattern.contains("{")) {
            // 文件名匹配
            return Pattern.compile(regex).matcher(fileName).find();
        } else {
            // 有时间信息,时间合规则放行
            Matcher matcher = Pattern.compile(regex).matcher(fileName);
            if (matcher.find()) {
                String timeString = matcher.group(0);
                return checkFileNameTimeValid(timeString, validDate);
            }
        }
        return false;
    }


    /**
     * 检查文件名时间是否有效
     *
     * @param timeString
     * @param validDate
     * @return
     */
    @SneakyThrows
    private boolean checkFileNameTimeValid(String timeString, Date validDate) {
        String yyyyMMdd = DateFormatUtils.format(validDate, "yyyyMMdd");
        int length = timeString.length();
        return Integer.parseInt(timeString) >= Integer.parseInt(yyyyMMdd.substring(0, length));
    }

    /**
     * 将自定义的路径表达式转换为正则表达式
     *
     * @param fileNameMatchPattern
     * @return
     */
    private String parsePathPatternToRegex(String fileNameMatchPattern) {
        if (fileNameMatchPattern.contains("*")) {
            return fileNameMatchPattern.replace("*", ".{1,100}");
        } else {
            // 时间匹配
            Matcher matcher = timePattern.matcher(fileNameMatchPattern);
            if (matcher.find()) {
                String group = matcher.group(0);
                return fileNameMatchPattern.replace(group, "(\\d{4}(\\d{2})?(\\d{2})?)");
            }
        }
        return fileNameMatchPattern;
    }


    @SneakyThrows
    private <T> T readJsonValue(String json, Class<T> clazz) {
        return objectMapper.readValue(json, clazz);
    }
}
