package com.njht.webyun.publish.index.service.impl;

import com.njht.webyun.publish.index.service.IndexService;
import com.njht.webyun.publish.index.vo.IndexBaseReqVo;
import com.njht.webyun.publish.index.vo.IndexReqVo;
import com.njht.webyun.publish.product.service.ProductInfoService;
import com.njht.webyun.publish.sys.constant.DicConstant;
import com.njht.webyun.publish.sys.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Author: 代国军
 * @CreateDate: 2021/11/22 13:49
 * @Description: 实现类
 */
@Service("indexService")
public class IndexServiceImpl implements IndexService {

    @Autowired
    private DicService dicService;

    @Autowired
    private ProductInfoService productInfoService;


    @Override
//    @Cacheable(value = IndexConstant.INDEX_INFO_REDIS,key = "#root.method.name")
    public IndexReqVo getIndexInfo() {
        return this.getIndexInfoFromDb();
    }

    private IndexReqVo getIndexInfoFromDb() {
        //标题 文字 信息从字典表中去取
        IndexBaseReqVo baseInfo = dicService.getTileInfo(DicConstant.PUBLISH_INDEX);
        //首页产品相关信息从产品表中取
        IndexReqVo reqVo =  productInfoService.getIndexInfo();
        reqVo.setBaseInfo(baseInfo);
        return reqVo;
    }
}
