package com.htht.job.admin.dispatch.controller;

import com.njht.entity.xxljob.XxlJobLogReport;
import com.htht.job.admin.dispatch.service.JobLogService;
import com.njht.webyun.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 代国军
 * @description: 调度成功率统计
 * @date 2022/7/26 14:53
 */
@RestController
@RequestMapping("/logReport")
public class JobLogReportController {

    @Autowired
    private JobLogService jobLogService;

    @PostMapping("/list")
    @ResponseBody
    public ReturnT<List<XxlJobLogReport>> list(@RequestParam("beginTime") String beginTime,
                                               @RequestParam("endTime") String endTime) {

        List<XxlJobLogReport> list = jobLogService.logReportList(beginTime,endTime);
        return ReturnT.success(list);
    }

}
