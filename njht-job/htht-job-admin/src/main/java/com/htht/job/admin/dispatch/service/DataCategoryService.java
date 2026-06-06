package com.htht.job.admin.dispatch.service;

import com.njht.webyun.entity.Tree;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
public interface DataCategoryService {

    /**
     * 查询任务目录树
     * @return
     */
    List<Tree> queryTreeListByRoleId();


    /**
     * 获取不同树结构的数据标识 以及treeId
     * @return
     */
    Map<String, List<String>> getTreeKeyMap();

    /**
     * 产品id
     * @return
     */
    String getProductId(String treeId);
}

