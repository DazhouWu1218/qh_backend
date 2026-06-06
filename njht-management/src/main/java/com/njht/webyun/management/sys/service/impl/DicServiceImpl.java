package com.njht.webyun.management.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.webyun.management.sys.dao.DicDao;
import com.njht.webyun.management.sys.entity.DicEntity;
import com.njht.webyun.management.sys.service.DicService;
import org.springframework.stereotype.Service;


/**
 * @author daiguojun
 */
@Service("dicService")
public class DicServiceImpl extends ServiceImpl<DicDao, DicEntity> implements DicService {


    @Override
    public String selectDownUrl() {
        return baseMapper.selectDownLoadUrl();
    }
}