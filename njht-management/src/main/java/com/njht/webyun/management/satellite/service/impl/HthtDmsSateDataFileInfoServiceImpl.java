package com.njht.webyun.management.satellite.service.impl;

import com.njht.webyun.management.satellite.dao.HthtDmsSateDataFileInfoDao;
import com.njht.webyun.management.satellite.entity.HthtDmsSateDataFileInfo;
import com.njht.webyun.management.satellite.service.HthtDmsSateDataFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author miaoyu
 * @date 2020-05-25 03:19:50
 */
@Service("hthtDmsSateDataFileInfoService")
public class HthtDmsSateDataFileInfoServiceImpl implements HthtDmsSateDataFileInfoService {
    /*<AUTOGEN--BEGIN>*/

    @Autowired
    public HthtDmsSateDataFileInfoDao hthtDmsSateDataFileInfoDao;

    /**
     * 存在即更新
     *
     * @param
     * @return
     */
    @Override
    public Integer upsert(HthtDmsSateDataFileInfo hthtDmsSateDataFileInfo) {
        return hthtDmsSateDataFileInfoDao.upsert(hthtDmsSateDataFileInfo);
    }

    /**
     * 存在即更新，可选择具体属性
     *
     * @param
     * @return
     */
    @Override
    public Integer upsertSelective(HthtDmsSateDataFileInfo hthtDmsSateDataFileInfo) {
        return hthtDmsSateDataFileInfoDao.upsertSelective(hthtDmsSateDataFileInfo);
    }

    @Override
    public List<HthtDmsSateDataFileInfo> query(HthtDmsSateDataFileInfo hthtDmsSateDataFileInfo) {
        return hthtDmsSateDataFileInfoDao.query(hthtDmsSateDataFileInfo);
    }

    @Override
    public Long queryTotal() {
        return hthtDmsSateDataFileInfoDao.queryTotal();
    }

    @Override
    public Integer deleteBatch(List<String> list) {
        return hthtDmsSateDataFileInfoDao.deleteBatch(list);
    }

    /*<AUTOGEN--END>*/

}
