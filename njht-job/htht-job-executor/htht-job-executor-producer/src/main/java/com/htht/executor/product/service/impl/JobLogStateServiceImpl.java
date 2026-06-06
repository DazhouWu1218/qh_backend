package com.htht.executor.product.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.htht.executor.product.dao.JobLogStateDao;
import com.htht.executor.product.entity.JobLogStateEntity;
import com.htht.executor.product.service.JobLogStateService;

/**
 * 
 * @author chenzedong
 * @date 2022-05-26 14:11:02
 */
@Service("jobLogStateService")
public class JobLogStateServiceImpl extends ServiceImpl<JobLogStateDao, JobLogStateEntity> implements JobLogStateService {

}