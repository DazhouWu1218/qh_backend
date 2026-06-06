package com.htht.executor.sys.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.executor.sys.constant.DicConstant;
import com.htht.executor.sys.dao.DicDao;
import com.htht.executor.sys.entity.DicEntity;
import com.htht.executor.sys.service.DicService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service("dicService")
@DS(value = "mysql")
public class DicServiceImpl extends ServiceImpl<DicDao, DicEntity> implements DicService {

    @Value("${satellite.linux.prePath}")
    private String satellitePath;

    /**
     * 字典信息
     * @param index
     * @return
     */
    private List<DicEntity> getDicList(String index) {
        QueryWrapper<DicEntity> qw = new QueryWrapper<>();
        qw.eq("DIC_TYPE",index);
        return this.list(qw);
    }

    @Override
    public String findSateUrl() {
        DicEntity dictCodeDTO = Optional.ofNullable(this.getOne(new LambdaQueryWrapper<DicEntity>()
                .eq(DicEntity::getDicType, DicConstant.SATELLITE_PATH)
                .eq(DicEntity::getDicKey,DicConstant.STATIC_RESOURCE)))
                .orElse(new DicEntity());
        return Objects.isNull(dictCodeDTO.getDicValue())? satellitePath: dictCodeDTO.getDicValue();
    }
}