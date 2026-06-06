package com.njht.webyun.business.index.service.impl;

import com.njht.entity.algorithm.AlgorithmEntity;
import com.njht.webyun.business.feign.AdminFeignService;
import com.njht.webyun.business.index.constant.IndexConstant;
import com.njht.webyun.business.index.service.AlgorithmService;
import com.njht.webyun.business.index.vo.AlgorithmCountReqVo;
import com.njht.webyun.business.index.vo.AlgorithmVo;
import com.njht.webyun.enums.TreeNodeEnum;
import com.njht.webyun.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 代国军
 * @description: 算法信息实现类
 * @date 2022/8/15 11:27
 */
@Service("algorithmService")
public class AlgorithmServiceImpl implements AlgorithmService {

    @Autowired
    private AdminFeignService adminFeignService;

    @Override
    public List<AlgorithmCountReqVo> algorithmCountInfo() {
        List<AlgorithmEntity> dbDataList = adminFeignService.algorithmNodeList().getData();

        // 计算算法总数并返回
        return Optional.ofNullable(dbDataList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> IndexConstant.INDEX_PARENT_ID.equals(item.getParentId()))
                .map(item -> {
                    AlgorithmCountReqVo reqVo = new AlgorithmCountReqVo();
                    reqVo.setId(item.getId());
                    reqVo.setName(item.getName());
                    List<AlgorithmEntity> algorithmList = new ArrayList<>();
                    this.setDataList(item,dbDataList,algorithmList);
                    reqVo.setCount(algorithmList.size());
                    return reqVo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PageUtils algorithmDetailInfo(AlgorithmVo algorithmVo) {
        return adminFeignService.algorithmPageList(algorithmVo).getData();
    }

    /**
     * 过滤出该目录节点下对应的数据节点信息（）计算个数或者展示用）
     * @param algorithmEntity
     * @param dbDataList
     * @return
     */
    @Override
    public void setDataList(AlgorithmEntity algorithmEntity, List<AlgorithmEntity> dbDataList, List<AlgorithmEntity> algorithmList) {
        String id = algorithmEntity.getId();
        // 获取 当前id 下的子集目录
        List<AlgorithmEntity> collect = Optional.ofNullable(dbDataList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> id.equals(item.getParentId()))
                .collect(Collectors.toList());
        for (AlgorithmEntity entity:collect) {
            // 判断当前节点是不是数据节点,是的话添加到数据返回节点中
            if (TreeNodeEnum.DATA.getCode().equals(entity.getType())) {
                algorithmList.add(entity);
            } else {
                this.setDataList(entity,dbDataList,algorithmList);
            }
        }
    }
}
