package com.htht.executor.download.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.htht.executor.download.dao.FtpDao;
import com.htht.executor.download.entity.FtpEntity;
import com.htht.executor.download.service.FtpService;

/**
 * 支撑平台-FTP配置信息表
 * @author daiguojun
 * @date 2022-05-17 16:41:22
 */
@Service("ftpService")
@DS(value = "mysql")
public class FtpServiceImpl extends ServiceImpl<FtpDao, FtpEntity> implements FtpService {


}