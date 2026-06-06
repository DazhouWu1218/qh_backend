package com.njht.webyun.management.dataanalysis.systeminformation.controller;

import com.njht.webyun.management.dataanalysis.systemdatacount.service.LogSystemDataCountService;
import com.njht.webyun.management.dataanalysis.systemdatacount.utils.FileCountUtils;
import com.njht.webyun.management.dataanalysis.systeminformation.service.PretreatmentDataService;
import com.njht.webyun.management.dataanalysis.systemvisit.service.SystemVisitCountService;
import com.njht.webyun.management.sys.service.UserService;
import com.njht.webyun.management.upload.dto.DiskCapacityDTO;
import com.njht.webyun.management.upload.service.DataAnalysisService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author Administrator
 */
@Slf4j
@RestController
@RequestMapping("/systemInfo")
public class SystemInfoController {


    @Autowired
    SystemVisitCountService systemVisitCountService;

    @Autowired
    UserService userService;

    @Autowired
    DataAnalysisService dataAnalysisService;

    @Autowired
    PretreatmentDataService pretreatmentDataService;

    @Autowired
    LogSystemDataCountService logSystemDataCountService;


    @Data
    public static class OverViewVo {

        private SystemVisitVo systemVisitVo;

        private DiskCapacityDTO diskCapacityDTO;

        private SystemDataVo systemDataVo;

        private BusinessAndPretreatmentVo businessAndPretreatmentVo;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class SystemVisitVo {
            private FileCountUtils.FileSizeDTO registerCount;
            private FileCountUtils.FileSizeDTO visitCount;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class SystemDataVo {
            private FileCountUtils.FileSizeDTO receiveCount;
            private FileCountUtils.FileSizeDTO downloadCount;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class BusinessAndPretreatmentVo {

            private FileCountUtils.FileSizeDTO businessProduct;

            private FileCountUtils.FileSizeDTO pretreatmentProduct;
        }


    }


    /**
     * 数据管理
     * <p>
     * 头部四个数据预览
     * 累计接收与分发
     * 业务生产与预处理生产
     * 磁盘储量
     * 注册人数和累计访问
     *
     * @return
     */
    @GetMapping("/overview")
    public ResponseEntity<OverViewVo> overview() {
        final OverViewVo overViewVo = new OverViewVo();
        /* ------ 统计系统访问总量 ------ */
        long start = System.currentTimeMillis();
        log.info(">>>开始统计[{}]>>>", "系统访问总量");
        try {
            final OverViewVo.SystemVisitVo systemVisitVo = new OverViewVo.SystemVisitVo();
            final Long visitCount = systemVisitCountService.selectAllCount();
            systemVisitVo.setVisitCount(new FileCountUtils.FileSizeDTO("人次", visitCount.toString()));
            final Long countUserNumber = userService.countUserNumber();
            systemVisitVo.setRegisterCount(new FileCountUtils.FileSizeDTO("人", countUserNumber.toString()));
            overViewVo.setSystemVisitVo(systemVisitVo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("!!!统计[{}]失败,异常:[{}]!!!", "系统访问总量", e.getMessage());
        }
        log.info("<<<结束统计[{}],耗时:[{}]<<<", "系统访问总量", System.currentTimeMillis() - start);

        /* ------ 统计系统累计接收/分发 ------ */
        start = System.currentTimeMillis();
        log.info(">>>开始统计[{}]>>>", "系统累计接收/分发");
        try {
            final Long allReceive = Optional.ofNullable(logSystemDataCountService.countAllHistory(false)).orElse(0L);
            final Long allDownload = Optional.ofNullable(logSystemDataCountService.countAllHistory(true)).orElse(0L);
            overViewVo.setSystemDataVo(new OverViewVo.SystemDataVo(FileCountUtils.byteToPrettyUnit(allReceive), FileCountUtils.byteToPrettyUnit(allDownload)));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("!!!统计[{}]失败,异常:[{}]!!!", "系统累计接收/分发", e.getMessage());
        }
        log.info("<<<结束统计[{}],耗时:[{}]<<<", "系统累计接收/分发", System.currentTimeMillis() - start);

        /* ------ 统计存储空间 ------ */
        start = System.currentTimeMillis();
        log.info(">>>开始统计[{}]>>>", "存储空间");
        try {
            DiskCapacityDTO diskCapacityDTO = dataAnalysisService.getDiskCapacity();
            overViewVo.setDiskCapacityDTO(diskCapacityDTO);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("!!!统计[{}]失败,异常:[{}]!!!", "存储空间", e.getMessage());
            // 发生异常,给个初始值0
            overViewVo.setDiskCapacityDTO(new DiskCapacityDTO());
        }
        log.info("<<<结束统计[{}],耗时:[{}]<<<", "存储空间", System.currentTimeMillis() - start);

        /* ------ 业务模块生产 预处理生产 ------ */
        start = System.currentTimeMillis();
        log.info(">>>开始统计[{}]>>>", "业务模块生产 预处理生产");
        try {
            final OverViewVo.BusinessAndPretreatmentVo businessAndPretreatmentVo = new OverViewVo.BusinessAndPretreatmentVo();
            overViewVo.setBusinessAndPretreatmentVo(businessAndPretreatmentVo);
            // 业务模块
            final Long businessStatisticInfo = dataAnalysisService.countAllBusinessStatisticInfo();
            businessAndPretreatmentVo.setBusinessProduct(FileCountUtils.byteToPrettyUnit(businessStatisticInfo));
            // 预处理
            final Long pretreatmentFileSizeCount = pretreatmentDataService.getPretreatmentFileSizeCount();
            businessAndPretreatmentVo.setPretreatmentProduct(FileCountUtils.byteToPrettyUnit(pretreatmentFileSizeCount));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("!!!统计[{}]失败,异常:[{}]!!!", "业务模块生产/预处理生产", e.getMessage());
        }
        log.info("<<<结束统计[{}],耗时:[{}]<<<", "业务模块生产 预处理生产", System.currentTimeMillis() - start);
        return ResponseEntity.ok(overViewVo);
    }


    @GetMapping("/fillCmissGribDataFile")
    public ResponseEntity fillCmissGribDataFile() {
        log.info("开始执行fillCmissGribDataFile >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        pretreatmentDataService.fillCmissGribDataFile();
        return ResponseEntity.ok("统计完成<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }

}
