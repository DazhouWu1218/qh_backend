package com.htht.data.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.data.category.dao.DataCategoryDao;
import com.htht.data.category.service.DataCategoryService;
import com.njht.entity.category.DataCategoryEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.njht.webyun.security.auth.AuthUtil.authLogic;

@Service("dataCategoryService")
@Slf4j
public class DataCategoryServiceImpl extends ServiceImpl<DataCategoryDao, DataCategoryEntity> implements DataCategoryService {

    @Autowired
    private DataCategoryService dataCategoryService;


    @Override
    public List<DataCategoryEntity> queryCategoryListByRoleId(List<String> roleIdList,String treeKey,String parentId) {
        return this.baseMapper.selectTreeListByRoleId(roleIdList,treeKey,parentId);
    }


    @Override
    public List<DataCategoryEntity> getCategoryList(String identify, String type) {
        List<String> roleIdList = authLogic.getRoleIdList();
        return dataCategoryService.queryCategoryListByRoleId(roleIdList,identify,type);
    }

    @Override
    public List<String> queryTreeKeyList() {
        QueryWrapper<DataCategoryEntity> qw = new QueryWrapper<>();
        qw.select("DISTINCT tree_key");
        List<DataCategoryEntity> list = this.list(qw);
        return Optional.ofNullable(list).orElse(new ArrayList<>())
                .stream()
                .map(DataCategoryEntity::getTreeKey).collect(Collectors.toList());
    }

    @Override
    public List<DataCategoryEntity> getParentCategoryInfoByProductIds(List<String> idList) {
        return baseMapper.getParentNameByProductIds(idList);
    }

}