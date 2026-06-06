package com.njht.webyun.management.satellite.service;


import com.njht.webyun.management.satellite.entity.HthtDmsSateDataFileInfo;

import java.util.List;

/**
 * service层
 *
 * @author miaoyu
 * @date 2020-05-25 03:19:50
 */
public interface HthtDmsSateDataFileInfoService {


    /**
     * 存在即更新
     *
     * @param hthtDmsSateDataFileInfo
     * @return
     */
    Integer upsert(HthtDmsSateDataFileInfo hthtDmsSateDataFileInfo);

    /**
     * 存在即更新，可选择具体属性
     *
     * @param hthtDmsSateDataFileInfo
     * @return
     */
    Integer upsertSelective(HthtDmsSateDataFileInfo hthtDmsSateDataFileInfo);

    List<HthtDmsSateDataFileInfo> query(HthtDmsSateDataFileInfo hthtDmsSateDataFileInfo);

    Long queryTotal();

    Integer deleteBatch(List<String> list);

    /*<AUTOGEN--END>*/


}
