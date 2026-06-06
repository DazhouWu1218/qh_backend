package com.htht.job.admin.feign;

import com.njht.entity.dataPush.SingleTaskExeEntity;
import com.njht.webyun.utils.ReturnT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 代国军
 * @description: 业务监控远程调用
 * @date 2022/7/26 15:08
 */
@FeignClient("njht-business")
public interface BusinessFeignService {

    @PostMapping({"/business/dataPush/logAlarm"})
    ReturnT<Boolean> logAlarm(@RequestBody SingleTaskExeEntity singleTaskExeEntity);

}
