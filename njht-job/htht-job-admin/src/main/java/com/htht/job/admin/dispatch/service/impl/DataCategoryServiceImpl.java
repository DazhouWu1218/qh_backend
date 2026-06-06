package com.htht.job.admin.dispatch.service.impl;

import com.htht.job.admin.dispatch.service.DataCategoryService;
import com.htht.job.admin.feign.DataCenterFeignService;
import com.njht.entity.base.BaseEntity;
import com.njht.entity.category.DataCategoryEntity;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.utils.TreeBuilderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataCategoryServiceImpl implements DataCategoryService {


    @Autowired
    private DataCenterFeignService dataCenterFeignService;

    @Override
    public List<Tree> queryTreeListByRoleId() {
        // 从数据中心获取树结构相关信息
        List<DataCategoryEntity> categoryEntityList = dataCenterFeignService.categoryList(null, null).getData();
        List<Tree> algorithmList = Optional.ofNullable(categoryEntityList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> !Objects.equals(item.getTreeKey(),"eco") && !Objects.equals(item.getTreeKey(),"nq"))
                .map(s -> {
                    Tree node = new Tree();
                    node.setValue(s.getId());
                    node.setLabel(s.getText());
                    node.setParentId(s.getParentId());
                    return node;
                }).collect(Collectors.toList());
        return TreeBuilderUtil.buildTreeList(algorithmList);
    }

    @Override
    public Map<String, List<String>> getTreeKeyMap() {
        // 返回结果 map
        Map<String,List<String>> returnMap = new HashMap<>(3);
        try {
            List<DataCategoryEntity> dbList = dataCenterFeignService.treeList().getData();
            log.info("数据获取成功......");
            Map<String, List<DataCategoryEntity>> map = Optional.ofNullable(dbList).orElse(new ArrayList<>())
                    .stream()
                    .filter(item -> !Objects.equals(item.getTreeKey(), "eco") && !Objects.equals(item.getTreeKey(), "nq"))
                    .collect(Collectors.groupingBy(DataCategoryEntity::getTreeKey));


            Optional.ofNullable(map).orElse(new HashMap<>(3))
                    .forEach((key,value) -> {
                        List<String> idList = this.getTreeKey(value);
                        returnMap.put(key,idList);
                    });
        } catch (Exception e) {
            e.printStackTrace();

        }
        return returnMap;
    }

    @Override
    public String getProductId(String treeId) {
        return dataCenterFeignService.getProductId(treeId).getData();
    }

    /**
     * 获取id 集合
     * @param categoryEntityList
     * @return
     */
    private List<String> getTreeKey(List<DataCategoryEntity> categoryEntityList) {
        return Optional.ofNullable(categoryEntityList).orElse(new ArrayList<>())
                .stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
    }

//    @Override
//    public List<String> getIdentifyList() {
//        List<String> list = new ArrayList<>();
//        try {
//            list = dataCenterFeignService.treeKeyList().getData();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    @Override
//    public List<String> queryTreeIdByType(String identify) {
//        List<DataCategoryEntity> data = dataCenterFeignService.categoryList(identify, null).getData();
//        return Optional.ofNullable(data).orElse(new ArrayList<>())
//                .stream()
//                .map(BaseEntity::getId)
//                .collect(Collectors.toList());
//    }

}