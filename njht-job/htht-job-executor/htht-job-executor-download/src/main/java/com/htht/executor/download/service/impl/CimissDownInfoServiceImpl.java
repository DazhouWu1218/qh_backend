package com.htht.executor.download.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.executor.download.dao.CimissDownInfoDao;
import com.htht.executor.download.entity.CimissDownInfoEntity;
import com.htht.executor.download.service.CimissDownInfoService;
import org.springframework.stereotype.Service;

@Service("cimissDownInfoService")
public class CimissDownInfoServiceImpl extends ServiceImpl<CimissDownInfoDao, CimissDownInfoEntity> implements CimissDownInfoService {
}
