package com.njht.webyun.business.index.service;

import com.njht.entity.algorithm.AlgorithmEntity;
import com.njht.webyun.business.index.vo.AlgorithmCountReqVo;
import com.njht.webyun.business.index.vo.AlgorithmVo;
import com.njht.webyun.utils.PageUtils;

import java.util.List;

/**
 * @author 代国军
 * @description: 算法相关信息
 * @date 2022/8/15 11:25
 */
public interface AlgorithmService {

    /**
     * 算法 信息
     * @return
     */
    List<AlgorithmCountReqVo> algorithmCountInfo();

    /**
     * 算法详情
     * @param algorithmVo
     * @return
     */
    PageUtils algorithmDetailInfo(AlgorithmVo algorithmVo);

    /**
     * 递归查找子节点
     * @param algorithmEntity
     * @param dbDataList
     * @param algorithmList
     */
    void setDataList(AlgorithmEntity algorithmEntity, List<AlgorithmEntity> dbDataList, List<AlgorithmEntity> algorithmList);
}
