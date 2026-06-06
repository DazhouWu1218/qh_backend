package com.njht.webyun.business.feign;

import com.njht.entity.algorithm.AlgorithmEntity;
import com.njht.entity.xxljob.CountInfo;
import com.njht.entity.xxljob.XxlJobLogReport;
import com.njht.webyun.business.index.vo.AlgorithmVo;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.ReturnT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author 代国军
 * @description: 调度模块远程调度service
 * @date 2022/7/26 15:08
 */
@FeignClient("njht-admin")
public interface AdminFeignService {

    @PostMapping({"/admin/logReport/list"})
    ReturnT<List<XxlJobLogReport>> logReportList(@RequestParam("beginTime")String beginTime, @RequestParam("endTime") String endTime);

    @GetMapping({"/admin/registry/list"})
    ReturnT<Map<String,Object>> registryList();

    @GetMapping({"/admin/algorithm/data/list"})
    ReturnT<List<AlgorithmEntity>> algorithmNodeList();

    @PostMapping({"/admin/algorithm/data/page"})
    ReturnT<PageUtils> algorithmPageList(@RequestBody AlgorithmVo algorithmVo);

    @GetMapping({"/admin/joblog/count"})
    ReturnT<CountInfo> count();
}
