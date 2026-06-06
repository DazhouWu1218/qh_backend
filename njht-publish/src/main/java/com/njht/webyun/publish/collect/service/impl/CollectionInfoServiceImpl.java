package com.njht.webyun.publish.collect.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.webyun.common.UserUtil;
import com.njht.webyun.publish.collect.dao.CollectionInfoDao;
import com.njht.webyun.publish.collect.entity.CollectionInfoEntity;
import com.njht.webyun.publish.collect.service.CollectionInfoService;
import com.njht.webyun.publish.product.entity.ProductFileInfoEntity;
import com.njht.webyun.publish.product.service.ProductFileInfoService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("collectionInfoService")
@Slf4j
public class CollectionInfoServiceImpl extends ServiceImpl<CollectionInfoDao, CollectionInfoEntity> implements CollectionInfoService {

    @Autowired
    private ProductFileInfoService productFileInfoService;

    @Autowired
    private CollectionInfoService collectionInfoService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CollectionInfoEntity> page = this.page(
                new Query<CollectionInfoEntity>().getPage(params),
                new QueryWrapper<CollectionInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveInfo(String ids, String types) {
        // 根据ids 以及types 返回要收藏的product_file_info_id
        List<ProductFileInfoEntity> productFileInfos = productFileInfoService.getProductFileInfos(ids, types);
        if(CollectionUtils.isEmpty(productFileInfos)){
            return;
        }
        List<CollectionInfoEntity> collect = productFileInfos.stream().map(item -> {
            CollectionInfoEntity entity = new CollectionInfoEntity();
            entity.setProductFileInfoId(item.getId());
            // 获取用户相关信息
            Integer userId = UserUtil.getCurrentUser().getUserId();
            entity.setCreatedBy(userId);
            entity.setLastUpdatedBy(userId);
            return entity;
        }).collect(Collectors.toList());
        //一条一条的入库没有入库的抛出异常信息
        for(CollectionInfoEntity entity:collect){
            try {
                collectionInfoService.saveOneCollectInfo(entity);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("用户id为{}的用户，已经收藏过id为：{}的数据。",entity.getCreatedBy(),entity.getProductFileInfoId());
            }
        }
    }


    /**
     * 添加收藏
     * @param entity
     */
//
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void saveOneCollectInfo(CollectionInfoEntity entity) {
        this.save(entity);
    }

}