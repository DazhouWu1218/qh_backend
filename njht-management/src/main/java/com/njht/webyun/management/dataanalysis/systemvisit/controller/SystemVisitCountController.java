package com.njht.webyun.management.dataanalysis.systemvisit.controller;

import com.njht.webyun.management.dataanalysis.systemvisit.dto.SystemVisitDateCountDTO;
import com.njht.webyun.management.dataanalysis.systemvisit.service.SystemVisitCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/systemVisitCount")
public class SystemVisitCountController {

    @Autowired
    SystemVisitCountService systemVisitCountService;

    /**
     * 增加访问次数
     *
     * @return
     */
    @GetMapping("/incrementVisitCount")
    public ResponseEntity incrementVisitCount() {
        final Long count = systemVisitCountService.incrementCount();
        return ResponseEntity.ok(count);
    }


    /**
     * 获取访问总量
     */
    @GetMapping("/selectAllCount")
    public Long selectAllCount() {
        return systemVisitCountService.selectAllCount();
    }


    /**
     * 用户访问统计
     * 周月年
     *
     * @return
     */
    @GetMapping("/statistics")
    public ResponseEntity statistics() {
        final Map<String, List<SystemVisitDateCountDTO>> statistics = systemVisitCountService.weekMonthYearStatistics();
        return ResponseEntity.ok(statistics);
    }


}
