package com.njht.webyun.business.index.service.impl;

import com.njht.entity.xxljob.CountInfo;
import com.njht.entity.xxljob.XxlJobLogReport;
import com.njht.webyun.business.common.util.TimeUtil;
import com.njht.webyun.business.feign.AdminFeignService;
import com.njht.webyun.business.feign.DataCenterFeignService;
import com.njht.webyun.business.index.constant.IndexConstant;
import com.njht.webyun.business.index.dto.TaskParam;
import com.njht.webyun.business.index.service.IndexService;
import com.njht.webyun.business.index.vo.MonitorReqVo;
import com.njht.webyun.business.index.vo.TimeParam;
import com.njht.webyun.utils.DateUtils;
import com.njht.webyun.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 代国军
 * @description: shixianlei
 * @date 2022/7/26 15:15
 */
@Service("indexService")
public class IndexServiceImpl implements IndexService {

    @Autowired
    private AdminFeignService adminFeignService;

    @Autowired
    private DataCenterFeignService dataCenterFeignService;


    @Override
    public List<MonitorReqVo> queryProductMonitorByType(String type) {
        List<MonitorReqVo> returnList = new ArrayList<>();
        TimeParam timeInfo = TimeUtil.getTimeInfo(type);

        Date beginDate = timeInfo.getBeginDate();
        Date endDate = timeInfo.getEndDate();

        // 数据通过feign 调用获取
        List<XxlJobLogReport> list = adminFeignService.logReportList(DateUtils.dateFormat(beginDate), DateUtils.dateFormat(endDate)).getData();

        // 数据根据treeKey 分组
        Map<String, List<XxlJobLogReport>> map = Optional.ofNullable(list).orElse(new ArrayList<>())
                .stream()
                .filter(item -> StringUtils.isNotEmpty(item.getTreeKey()))
                .collect(Collectors.groupingBy(XxlJobLogReport::getTreeKey));
        // 添加返回结果
        map.forEach((key, value) -> {
            MonitorReqVo reqVo = new MonitorReqVo();
            reqVo.setName(key);
            reqVo.setValue(key);
            this.getTaskList(value,reqVo);
            returnList.add(reqVo);
        });
        return returnList;
    }





    @Override
    public CountInfo count() {
        // 从调度中心获取任务，执行器，以及调度总数
        CountInfo countInfo = adminFeignService.count().getData();
        countInfo.setProductCount(dataCenterFeignService.count().getData());
        return countInfo;

    }


    /**
     * 获取运行中，成功，失败的任务总数
     * @param logReportInfoList
     * @return
     */
    private void getTaskList(List<XxlJobLogReport> logReportInfoList,MonitorReqVo reqVo) {
        // 某一种类型的数据成功率
        List<TaskParam> taskParamList = new ArrayList<>();
        long successCount = logReportInfoList.stream().mapToLong(XxlJobLogReport::getSucCount).sum();
        TaskParam successParam = new TaskParam(IndexConstant.INDEX_PRODUCT_SUCCESS, IndexConstant.INDEX_TASK_SUCCESS_MSG, successCount);
        taskParamList.add(successParam);
        long runCount = logReportInfoList.stream().mapToLong(XxlJobLogReport::getRunningCount).sum();
        TaskParam runParam = new TaskParam(IndexConstant.INDEX_PRODUCT_EXECUTING, IndexConstant.INDEX_TASK_RUN_MSG, runCount);
        taskParamList.add(runParam);
        long failCount = logReportInfoList.stream().mapToLong(XxlJobLogReport::getFailCount).sum();
        TaskParam failParam = new TaskParam(IndexConstant.INDEX_PRODUCT_FAILED, IndexConstant.INDEX_TASK_FAIL_MSG, failCount);
        taskParamList.add(failParam);
        reqVo.setTaskList(taskParamList);

        // 任务总数
        long sum = successCount + runCount + failCount;
        reqVo.setSumNum(sum);
    }
}
