package com.njht.webyun.publish.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.publish.index.vo.IndexBaseReqVo;
import com.njht.webyun.publish.sys.entity.DicEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-22 11:54:17
 */
public interface DicService extends IService<DicEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取发布平台首页标题以及相关信息
     * @param index
     * @return
     */
    IndexBaseReqVo getTileInfo(String index);

    /**
     * 获取发布平台用户反馈产品分类信息
     * @return
     */
    List<CommonEntity> getFeedBackCategoryInfo();


    Map<String,String> getFeedBackCategoryMap();

    /**
     * 产品标识对应关系
     * @return
     */
    List<CommonEntity> getIdentifyList();

}

