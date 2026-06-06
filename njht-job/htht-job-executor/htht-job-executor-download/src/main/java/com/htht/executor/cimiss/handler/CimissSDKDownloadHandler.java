package com.htht.executor.cimiss.handler;

import com.htht.executor.cimiss.service.impl.CimissSDKDownloadServiceImpl;
import com.htht.executor.cimiss.service.impl.CimissSDKFileDownloadServiceImpl;
import com.htht.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CimissSDKDownloadHandler {

    @Autowired
    private CimissSDKDownloadServiceImpl cimissSDKDownloadService;

    @Autowired
    private CimissSDKFileDownloadServiceImpl cimissSDKFileDownloadService;

    @XxlJob("cimissSDKDownloadHandler")
    public void cimissDataSDKDown() throws Exception {
        cimissSDKDownloadService.down();
    }

    @XxlJob("cimissSDKFileDownloadHandler")
    public void cimissSDKFileDownloadHandler() throws Exception {
        cimissSDKFileDownloadService.down();
    }



}
