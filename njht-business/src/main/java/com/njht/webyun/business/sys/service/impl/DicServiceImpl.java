package com.njht.webyun.business.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.webyun.business.datapush.constant.EsConstant;
import com.njht.webyun.business.sys.dao.DicDao;
import com.njht.webyun.business.sys.entity.DicEntity;
import com.njht.webyun.business.sys.service.DicService;
import com.njht.webyun.entity.CommonEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service("dicService")
public class DicServiceImpl extends ServiceImpl<DicDao, DicEntity> implements DicService {

    /**
     * 字典信息
     * @param index
     * @return
     */
    private List<DicEntity> getDicList(String index) {
        QueryWrapper<DicEntity> qw = new QueryWrapper<>();
        qw.eq("DIC_TYPE",index);
        return list(qw);
    }

    public List<CommonEntity> getIdentifyList() {
        List<CommonEntity> commonEntityList = new ArrayList<>();
        // 将枚举信息塞到对象中并返回给前端
        List<DicEntity> dicList = getDicList("CATEGORY_TREE");

        for (DicEntity item:dicList) {
            CommonEntity entity = new CommonEntity(item.getDicKey(),item.getDicValue());
            commonEntityList.add(entity);
        }
        return commonEntityList;
    }

    @Override
    public List<DicEntity>  getDataPushInfo(String type) {
        return getDicList(type);
    }

    @Override
    public String getDiUrl() {
        List<DicEntity> dicList = getDicList(EsConstant.BUSINESS_DI_EI_URL);
        return Optional.ofNullable(dicList).orElse(new ArrayList<>())
                .stream()
                .findAny()
                .orElse(new DicEntity(EsConstant.DI_URL)).getDicValue();
    }
}