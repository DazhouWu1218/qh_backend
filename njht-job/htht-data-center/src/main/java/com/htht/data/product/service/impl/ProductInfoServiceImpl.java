package com.htht.data.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.data.product.dao.ProductInfoDao;
import com.htht.data.product.entity.ProductFileInfoEntity;
import com.htht.data.product.entity.ProductInfoEntity;
import com.htht.data.product.service.ProductFileInfoService;
import com.htht.data.product.service.ProductInfoService;
import com.njht.webyun.common.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 
 * @author daiguojun
 * @date 2022-01-05 09:15:29
 */
@Service("productInfoService")
@Slf4j
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoDao, ProductInfoEntity> implements ProductInfoService {


    @Autowired
    private ProductFileInfoService productFileInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editProductStatus(List<String> ids) {
        //根据产品id把产品的 is_show 字段修改为已发布状态
        List<ProductInfoEntity> collect = Optional.ofNullable(ids).orElse(new ArrayList<>()).stream().map(s -> {
            ProductInfoEntity productInfoEntity = new ProductInfoEntity();
            productInfoEntity.setId(s);
            productInfoEntity.setIsRelease(1);
            return productInfoEntity;
        }).collect(Collectors.toList());
        this.updateBatchById(collect);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductById(String id) {
        // 刪除该区域这一期次的所有产品。
        ProductInfoEntity entity = this.getById(id);
        String mark = entity.getMark();
        String cycle = entity.getCycle();
        String issue = entity.getIssue();
        QueryWrapper<ProductInfoEntity> qw = new QueryWrapper<>();
        qw.select("id");
        qw.eq("issue",issue).eq("cycle",cycle).eq("mark",mark);
        //查询所有要删除的数据信息
        List<ProductInfoEntity> list = this.list(qw);
        List<String> idList =
                Optional.ofNullable(list).orElse(new ArrayList<>()).stream().map(ProductInfoEntity::getId).collect(Collectors.toList());
        //根据产品id 批量删除
        this.removeByIds(idList);

        //根据产品id批量删除文件信息
        QueryWrapper<ProductFileInfoEntity> fileQw = new QueryWrapper<>();
        fileQw.in("product_info_id",idList);
        productFileInfoService.remove(fileQw);
        log.info("用户：[{}],删除了标识为[{}]产品,该产品的期次为[{}]，周期为[{}]", UserUtil.getCurrentUser().getUserId(),mark,issue,cycle);
    }

}