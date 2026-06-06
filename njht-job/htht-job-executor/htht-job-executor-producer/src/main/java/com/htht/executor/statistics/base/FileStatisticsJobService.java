package com.htht.executor.statistics.base;

import com.alibaba.fastjson.JSON;
import com.htht.executor.fileStatistic.service.DataBaseInfoService;
import com.htht.executor.fileStatistic.service.DataReportService;
import com.htht.executor.statistics.constant.FileStatisticsTypeConstant;
import com.htht.executor.statistics.param.StatisticsParam;
import com.htht.executor.task.constant.BaseProductServiceConstant;
import com.htht.executor.task.util.CalendarUtil;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.constant.DateConstant;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.exception.CommonException;
import com.htht.job.core.util.DateUtil;
import com.htht.job.core.util.FileNameUtils;
import com.htht.job.core.util.FileUtil;
import com.htht.job.core.util.MatchTime;
import com.njht.entity.dataReport.DataBaseInfoEntity;
import com.njht.entity.dataReport.DataReportEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author zhushizhen
 * @Date 2022-09-26 17:21
 **/
@Slf4j
@Transactional(rollbackFor = Exception.class)
public abstract class FileStatisticsJobService {

    @Autowired
    private DataBaseInfoService dataBaseInfoService;

    @Autowired
    private DataReportService dataReportService;

    private static String END_HOUR_MINUTE = "2350";

    private static String END_HOUR = "2300";
    private static String START_HOUR = "0000";

    /**
     * 分片方法（无需查重，只根据实时/历史分片--YYYYMMDD）
     *
     * @param taskParam
     * @return
     */
    public ReturnT<List<String>> executeShard(TaskParam taskParam) {
        ReturnT<List<String>> result = new ReturnT<>();
        // 页面模板参数
        String params = taskParam.getModelParameters();
        StatisticsParam statisticsParam = JSON.parseObject(params, StatisticsParam.class);
        /*** ==========1.获取开始和结束日期========== ***/
        List<String> dateRanges = this.getBeginAndEndTimeList(statisticsParam);
        XxlJobHelper.log("总共需要处理：{} 期数据;", dateRanges.size());
        result.setData(dateRanges);
        return result;
    }

    /**
     * 获取开始和结束之间的天数
     *
     * @param statisticsParam
     * @return
     */
    protected List<String> getBeginAndEndTimeList(StatisticsParam statisticsParam) {
        //村塾数据
        List<String> list = new ArrayList<>();
        try {
            /****1.获取开始和结束时间***/
            Date endTime = new Date();
            Date beginTime = new Date();
            if (Objects.equals(statisticsParam.getDateType(), FileStatisticsTypeConstant.DATE_TYPE_NOW)) {
                int days = Integer.parseInt(statisticsParam.getProductRangeDay());
                beginTime = DateUtil.addHour(endTime, -days);
            } else if (Objects.equals(statisticsParam.getDateType(), FileStatisticsTypeConstant.DATE_TYPE_HISTORY)) {
                String[] temp = statisticsParam.getProductRangeDate().split(",");
                beginTime = new SimpleDateFormat(DateConstant.YYYY_MM_DD_HH_MM_SS).parse(temp[0]);
                endTime = new SimpleDateFormat(DateConstant.YYYY_MM_DD_HH_MM_SS).parse(temp[1]);
            }
            /****2.获取开始和结束时间所有的天数***/
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(beginTime);
            while (calendar.getTime().before(endTime)) {
                list.add(DateFormatUtils.format(calendar.getTime(), "yyyyMMdd") + "0000");
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            return list;
        } catch (Exception e) {
            throw new CommonException("数据开始时间和结束时间所有的期次失败");
        }
    }

    /**
     * handler统计方法具体实现
     * @param triggerParam
     * @return
     */
    public ReturnT execute(TriggerParam triggerParam) {
        ReturnT<String> result = new ReturnT<>();
        try {
            /*** ==========1、获取分片日期，查找BASE表中基本配置信息========== ***/
            String baseIssue = triggerParam.getExecutorParams();
            XxlJobHelper.log("当前分片时间为：{};", baseIssue);
            //获取任务参数
            TaskParam taskParam = triggerParam.getTaskParam();
            // 页面模板参数
            String params = taskParam.getModelParameters();
            //封装到对象中
            StatisticsParam statisticsParam = JSON.parseObject(params, StatisticsParam.class);
            //从数据库中获取文件正则和文件目录等信息
            List<DataBaseInfoEntity> dataBaseInfoEntityList = dataBaseInfoService.selectBaseInfo(statisticsParam);
            //循环处理配置信息
            XxlJobHelper.log("开始读取{}配置信息：", statisticsParam.getDataType());
            for(DataBaseInfoEntity dataBaseInfoEntity:dataBaseInfoEntityList){
                XxlJobHelper.log("当前执行{}配置任务：", dataBaseInfoEntity.getName());
                List<String> issueList;
                /*** ==========2、判断report表中预报信息是否存在========== ***/
                //产品支持多周期预报信息添加
                List<String> cycleList = new ArrayList<>();
                CollectionUtils.addAll(cycleList, dataBaseInfoEntity.getCycle().split(","));
                //先进行多周期处理
                for (String cycle : cycleList) {
                    //判断预报信息是否添加在report表中
                    issueList = this.isExistReportInfo(dataBaseInfoEntity, baseIssue, cycle);
                    if (issueList.isEmpty()) {
                        XxlJobHelper.log("{}配置周期({})无需处理/不支持！已跳过", dataBaseInfoEntity.getName(), cycle);
                        continue;
                    }

                    /*** ==========3、扫描文件是否存在========== ***/
                    //原始和预处理扫描文件（产品查表）
                    List<DataReportEntity> updateReportList = updateReportInfo(dataBaseInfoEntity, issueList, cycle);

                    /*** ==========4、更新报告表记录信息========== ***/
                    XxlJobHelper.log("{}配置统计信息开始更新", dataBaseInfoEntity.getName());
                    if (!updateReportList.isEmpty()) {
                        dataReportService.updateAll(updateReportList);
                        XxlJobHelper.log("{}配置统计信息更新成功", dataBaseInfoEntity.getName());
                    } else {
                        XxlJobHelper.log("{}配置统计信息无数据更新", dataBaseInfoEntity.getName());
                    }
                }
            }
            result.setCode(ReturnT.SUCCESS_CODE);
        } catch (Exception e) {
            XxlJobHelper.log("出现异常：" + e);
            result.setCode(ReturnT.FAIL_CODE);
            throw e;
        }
        return result;
    }

    /**
     * 根据路径和正则扫描文件，形成report统计信息
     *
     * @param dataBaseInfoEntity
     * @param issueList
     * @return
     */
    protected List<DataReportEntity> updateReportInfo(DataBaseInfoEntity dataBaseInfoEntity, List<String> issueList, String cycle) {
        List<DataReportEntity> updateReportList = new ArrayList<>();
        String tempFilePath = dataBaseInfoEntity.getFilePath();
        String tempFileRegex = dataBaseInfoEntity.getFileRegex();

        XxlJobHelper.log("进行文件扫描：");
        issueList.forEach(issue -> {
            XxlJobHelper.log("开始查找{}期次数据", issue);
            //时间校正，世界时-8
            Date currentTime = new Date();
            Date time = DateUtil.strToDate(issue, DateConstant.YYYYMMDDHHMM);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentTime);
            calendar.add(Calendar.HOUR_OF_DAY, dataBaseInfoEntity.getCorrectionTime());
            currentTime = calendar.getTime();
            //文件超过当前系统时间跳过循环
            if (time.after(currentTime)) {
                XxlJobHelper.log("{}期次超过当前系统时间，数据无需统计！", issue);
                return;
            }
            String filePath = tempFilePath;
            String fileRegex = tempFileRegex;
            //替换路径和正则中的时间字符
            filePath = FileNameUtils.dealFilePath(filePath, issue);
            fileRegex = FileNameUtils.dealFilePath(fileRegex, issue);
            if (!new File(filePath).exists()) {
                XxlJobHelper.log("扫描目录({})不存在，数据无需统计！", filePath);
                return;
            }
            List<File> scanFileList = FileUtil.iteratorFileAndDirectory(filePath, fileRegex);
            if (scanFileList.isEmpty()) {
                XxlJobHelper.log("未找到{}期次文件数据，数据无需统计！", issue);
                return;
            } else {
                XxlJobHelper.log("找到期次为{}文件，个数为：{},", issue, scanFileList.size());
            }
            Long fileNum = (long) scanFileList.size();
            String status = "0";
            Integer sumNum = dataBaseInfoEntity.getSumNum().intValue();
            if (scanFileList.size() < sumNum) {
                status = "2";
            } else if (scanFileList.size() == sumNum) {
                status = "1";
            }
            Long fileSize = 0L;
            fileSize = scanFileList.stream().mapToLong(File::length).sum();

            DataReportEntity dataReportEntity = new DataReportEntity();
            dataReportEntity.setIssue(issue);
            dataReportEntity.setFileNum(fileNum);
            dataReportEntity.setSumNum(dataBaseInfoEntity.getSumNum());
            dataReportEntity.setFileSize(fileSize);
            dataReportEntity.setStatus(status);
            dataReportEntity.setDataId(dataBaseInfoEntity.getId());

            updateReportList.add(dataReportEntity);
        });
        return updateReportList;
    }

    /**
     * 判断report表中baseId对应的issue是否存在
     * <p>
     * 返回值说明：始终返回基于周期生成的预报期次List
     *
     * @param dataBaseInfoEntity
     */
    protected List<String> isExistReportInfo(DataBaseInfoEntity dataBaseInfoEntity, String baseIssue, String cycle) {
        List<DataReportEntity> resultList = new ArrayList<>();
        //查询数据库中已入库的期次数据
        List<DataReportEntity> dataReportEntityList = new ArrayList<>();
        if (BaseProductServiceConstant.COAY.equals(cycle)|| BaseProductServiceConstant.COAQ.equals(cycle)|| BaseProductServiceConstant.COAM.equals(cycle)) {
            //年、季、月 按YYYY进行期次匹配
            dataReportEntityList = dataReportService.selectReportEntity(dataBaseInfoEntity.getId(), baseIssue.substring(0, 4), cycle);
        }else if(BaseProductServiceConstant.COOD.equals(cycle)|| BaseProductServiceConstant.COOH.equals(cycle)|| BaseProductServiceConstant.COTM.equals(cycle)){
            //日、实时、10分钟按YYYYMMDD进行期次匹配
            dataReportEntityList = dataReportService.selectReportEntity(dataBaseInfoEntity.getId(), baseIssue.substring(0, 8), cycle);
        }
        List<String> issueList = Optional.of(dataReportEntityList.stream().map(DataReportEntity::getIssue).collect(Collectors.toList())).orElse(new ArrayList<>());
        //获取该周期数据应该到报的期次
        List<String> autoReportIssueList = this.getIssueListByCycle(dataBaseInfoEntity.getCycle(), dataBaseInfoEntity.getCorrectionTime(), baseIssue,dataBaseInfoEntity.getTimeRange());
        List<String> resultAllIssueList = new ArrayList<>(autoReportIssueList);
        //去除数据中已经存在的数据
        if (!issueList.isEmpty()) {
            autoReportIssueList.removeAll(issueList);
        }
        //过滤出数据已经完全到报的期次
        List<DataReportEntity> filterDataList = dataReportEntityList.stream().filter(dataReportEntity -> Objects.equals(dataReportEntity.getStatus(), "1")).collect(Collectors.toList());
        List<String> completeIssueList = Optional.of(filterDataList.stream().map(DataReportEntity::getIssue).collect(Collectors.toList())).orElse(new ArrayList<>());
        resultAllIssueList.removeAll(completeIssueList);
        if (autoReportIssueList.isEmpty()) {
            return resultAllIssueList;
        }
        //整理应存在但是数据库中缺失时间的数据对象
        for (String issue : autoReportIssueList) {
            DataReportEntity dataReportEntity = new DataReportEntity();
            dataReportEntity.setId(UUID.randomUUID().toString().replace("-", ""));
            dataReportEntity.setVersion(0);
            dataReportEntity.setIssue(issue);
            dataReportEntity.setCycle(cycle);
            dataReportEntity.setFileNum(0L);
            dataReportEntity.setSumNum(dataBaseInfoEntity.getSumNum());
            dataReportEntity.setFileSize(0L);
            dataReportEntity.setIdentify(dataBaseInfoEntity.getIdentify());
            dataReportEntity.setStatus("0");
            dataReportEntity.setDataId(dataBaseInfoEntity.getId());
            dataReportEntity.setDataName(dataBaseInfoEntity.getName());
            resultList.add(dataReportEntity);
        }
        //批量插入缺失时间的数据
        dataReportService.saveAll(resultList);
        return resultAllIssueList;

    }

    /**
     * 根据周期类型生成issue(COTM/COOH/COOD/COAM/COAQ/COAY)
     *
     * @param cycle
     * @return
     */
    protected List<String> getIssueListByCycle(String cycle, int correctionTime, String baseIssue,String timeRange) {
        List<String> issueList = new ArrayList<>();
        switch (cycle) {
            case BaseProductServiceConstant.COTM:
            case BaseProductServiceConstant.COOH: {
                issueList = getIssueHourOrMinuteList(cycle,correctionTime,baseIssue,timeRange);
                break;
            }
            case BaseProductServiceConstant.COOD: {
                issueList.add(baseIssue);
                break;
            }
            case BaseProductServiceConstant.COAM: {
                issueList.add(baseIssue.substring(0,6)+"010000");
                break;
            }
            case BaseProductServiceConstant.COAQ: {
                String month = baseIssue.substring(4,6);
                if(month.equals("01")||month.equals("02")||month.equals("03")){
                    issueList.add(baseIssue.substring(0,4)+"01010000");
                }else if(month.equals("04")||month.equals("05")||month.equals("06")){
                    issueList.add(baseIssue.substring(0,4)+"04010000");
                }else if(month.equals("07")||month.equals("08")||month.equals("09")){
                    issueList.add(baseIssue.substring(0,4)+"07010000");
                }else if(month.equals("10")||month.equals("11")||month.equals("12")){
                    issueList.add(baseIssue.substring(0,4)+"10010000");
                }
                break;
            }
            case BaseProductServiceConstant.COAY: {
                issueList.add(baseIssue.substring(0,4)+"01010000");
                break;
            }
            default:
                break;
        }
        return issueList;
    }

    private List<String> getIssueHourOrMinuteList(String cycle, int correctionTime, String baseIssue,String timeRange) {
        List<String> issueList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        // 获取开始结束时间
        List<String> list;
        String endRange = null;
        if (!StringUtils.isEmpty(timeRange)) {
            try {
                list =  Arrays.asList(timeRange.split("-"));
                // 获取开始时间
                String start = Optional.of(list).orElse(new ArrayList<>())
                        .stream()
                        .findFirst().orElse(START_HOUR);
                // 更新 baseIssue
                baseIssue = baseIssue.substring(0,8)+start.substring(0,4);
                // 获取 结束 小时 分钟
                if (list.size() >= 2){
                    endRange = list.get(1).substring(0,4);
                }
            } catch (Exception e) {
                log.error("时间范围配置错误");
                e.printStackTrace();
            }
        }
        //校正时间
        Date startTime = DateUtil.strToDate(baseIssue, DateConstant.YYYYMMDDHHMM);
        calendar.setTime(startTime);
        calendar.add(Calendar.HOUR_OF_DAY, correctionTime);
        startTime = calendar.getTime();
        Date endTime = new Date();
        if(BaseProductServiceConstant.COTM.equals(cycle)) {
            endRange = StringUtils.isEmpty(endRange)?END_HOUR_MINUTE:endRange;
            endTime = DateUtil.strToDate(baseIssue.substring(0, 8) + endRange, DateConstant.YYYYMMDDHHMM);
        }else if(BaseProductServiceConstant.COOH.equals(cycle)){
            endRange = StringUtils.isEmpty(endRange)?END_HOUR:endRange;
            endTime = DateUtil.strToDate(baseIssue.substring(0, 8) + endRange, DateConstant.YYYYMMDDHHMM);
        }
        calendar.setTime(endTime);
        calendar.add(Calendar.HOUR_OF_DAY, correctionTime);
        endTime = calendar.getTime();
        calendar.setTime(endTime);
        while (calendar.getTimeInMillis() >= startTime.getTime()) {
            String issue = MatchTime.matchIssue(calendar.getTime(), cycle);
            calendar = CalendarUtil.calendarDealLast(calendar, cycle);
            if (issueList.contains(issue)) {
                continue;
            }
            issueList.add(issue);
        }
        return issueList;
    }

}
