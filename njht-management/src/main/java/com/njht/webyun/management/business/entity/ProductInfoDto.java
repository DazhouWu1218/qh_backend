package com.njht.webyun.management.business.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
public class ProductInfoDto extends ProductInfo implements Serializable {

    private List<ProductFileInfo> productFileInfos;
    private List<ProductFileInfoForgien> productFileInfoList;

    public ProductInfoDto() {
    }


    public List<ProductFileInfo> getProductFileInfos() {
        return productFileInfos;
    }

    public void setProductFileInfos(List<ProductFileInfo> productFileInfos) {
        this.productFileInfos = productFileInfos;
    }

    public List<ProductFileInfoForgien> getProductFileInfoList() {
        return productFileInfoList;
    }

    public void setProductFileInfoList(List<ProductFileInfoForgien> productFileInfoList) {
        this.productFileInfoList = productFileInfoList;
    }
}