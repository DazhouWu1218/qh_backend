package com.njht.webyun.business.datapush.controller;

import com.njht.entity.dataPush.SingleTaskExeEntity;
import com.njht.webyun.business.datapush.handler.DataPushHandler;
import com.njht.webyun.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 数据推送
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-08-16 15:49:24
 */
@RestController
@RequestMapping("dataPush")
public class DataPushController {

    @Autowired
    private DataPushHandler dataPushHandler;

    @PostMapping("logAlarm")
    public ReturnT<Boolean> logAlarm(@RequestBody SingleTaskExeEntity singleTaskExeEntity) {
        return ReturnT.success(dataPushHandler.logAlarm(singleTaskExeEntity));
    }

    @GetMapping("list")
    public ReturnT list() {
        dataPushHandler.dataPushHandler();
        return ReturnT.success();
    }


}
