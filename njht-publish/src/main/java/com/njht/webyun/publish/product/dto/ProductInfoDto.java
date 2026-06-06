package com.njht.webyun.publish.product.dto;

import com.njht.webyun.publish.product.entity.ProductFileInfoEntity;
import com.njht.webyun.publish.product.entity.ProductInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/12/8 20:11
 * @Description: 业务产品相关
 */
@Data
public class ProductInfoDto extends ProductInfoEntity {

    private List<ProductFileInfoEntity> fileInfoEntityList;
}
