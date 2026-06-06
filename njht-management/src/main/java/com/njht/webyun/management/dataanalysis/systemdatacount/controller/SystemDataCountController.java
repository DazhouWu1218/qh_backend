package com.njht.webyun.management.dataanalysis.systemdatacount.controller;

import com.njht.webyun.management.dataanalysis.systemdatacount.dto.LogSystemDataCountByDayDTO;
import com.njht.webyun.management.dataanalysis.systemdatacount.service.LogSystemDataCountDiskService;
import com.njht.webyun.management.dataanalysis.systemdatacount.service.LogSystemDataCountService;
import com.njht.webyun.management.dataanalysis.systemdatacount.utils.FileCountUtils;
import com.njht.webyun.management.dataanalysis.systemdatacount.vo.LogSystemDataByDataTypeVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统数据 接收分发统计
 *
 * @author zhouhouliang
 */
@Api(tags = "")
@Slf4j
@RestController
@RequestMapping("/systemDataCount")
public class SystemDataCountController {

    @Autowired
    LogSystemDataCountService logSystemDataCountService;


    /**
     * 扫描磁盘统计数据
     */
    @Autowired
    LogSystemDataCountDiskService logSystemDataCountDiskService;

    /**
     * 系统数据接收,分发 周月年统计
     * 周月年
     *
     * @return
     */
    @GetMapping("/statisticsByTime")
    public ResponseEntity statisticsByTime(Boolean isDownload) {
        log.info("系统数据接收分发: 周月年统计");
        final List<LogSystemDataCountByDayDTO> w = logSystemDataCountService.countWeek(isDownload).stream().map(logSystemDataCountByDayDTO -> {
            logSystemDataCountByDayDTO.setCount(FileCountUtils.byteToGB(Long.valueOf(logSystemDataCountByDayDTO.getCount())));
            return logSystemDataCountByDayDTO;
        }).collect(Collectors.toList());
        final List<LogSystemDataCountByDayDTO> m = logSystemDataCountService.countMonth(isDownload).stream().map(logSystemDataCountByDayDTO -> {
            logSystemDataCountByDayDTO.setCount(FileCountUtils.byteToGB(Long.valueOf(logSystemDataCountByDayDTO.getCount())));
            return logSystemDataCountByDayDTO;
        }).collect(Collectors.toList());
        final List<LogSystemDataCountByDayDTO> y = logSystemDataCountService.countYear(isDownload).stream().map(logSystemDataCountByDayDTO -> {
            logSystemDataCountByDayDTO.setCount(FileCountUtils.byteToGB(Long.valueOf(logSystemDataCountByDayDTO.getCount())));
            return logSystemDataCountByDayDTO;
        }).collect(Collectors.toList());

        final LinkedHashMap<String, List<LogSystemDataCountByDayDTO>> resultMap = new LinkedHashMap<>();
        resultMap.put("week", w);
        resultMap.put("month", m);
        resultMap.put("year", y);
        return ResponseEntity.ok(resultMap);
    }


    /**
     * 系统数据接收分发: 按照数据类型统计
     *
     * @param isDownload
     * @return
     */
    @GetMapping("/statisticByDataType")
    public ResponseEntity statisticByDataType(Boolean isDownload) {
        log.info("系统数据接收分发: 按照数据类型统计");
        final List<LogSystemDataByDataTypeVO> logSystemDataByDataTypeVOS = logSystemDataCountService.statisticByDataType(isDownload);
        return ResponseEntity.ok(logSystemDataByDataTypeVOS);
    }

    /**
     * 重新统计磁盘数据接收量
     *
     * @return
     */
    @GetMapping("/reCalculateReceive")
    public ResponseEntity reCalculateReceive() {
        log.info("重新统计磁盘数据接收量");
        final long start = System.currentTimeMillis();
        logSystemDataCountDiskService.calculateDataReceive();
        return ResponseEntity.ok("完成统计,耗时:" + (System.currentTimeMillis() - start) + "ms");
    }


}
