package com.njht.webyun.publish.sys.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.webyun.constant.DbConstant;
import com.njht.webyun.publish.sys.dao.OrgDao;
import com.njht.webyun.publish.sys.entity.OrgEntity;
import com.njht.webyun.publish.sys.service.OrgService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("orgService")
@DS(DbConstant.MYSQL)
public class OrgServiceImpl extends ServiceImpl<OrgDao, OrgEntity> implements OrgService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrgEntity> page = this.page(
                new Query<OrgEntity>().getPage(params),
                new QueryWrapper<OrgEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public String queryRegionIdByUserId(Integer userId) {
        return baseMapper.selectRegionIdByUserId(userId);
    }

}