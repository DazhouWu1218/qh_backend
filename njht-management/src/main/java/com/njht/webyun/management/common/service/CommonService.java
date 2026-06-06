package com.njht.webyun.management.common.service;

import com.njht.webyun.utils.ReturnT;

/**
 * @author ：daiguojun
 * @date ：Created in 2021/3/3 16:36
 * @description：公用的service层
 */
public interface CommonService {

    /**
     * 查询树形结构
     * @param identify
     * @return
     */
    ReturnT<Object> getProductTree(String identify);

}
