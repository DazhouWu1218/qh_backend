package com.njht.webyun.product.constant;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/18 11:02
 * @Description: 产品相关常量定义
 */
public class ProductConstant {

    private ProductConstant() {}

    /**
     * 产品标识
     */
    public  static final String PRODUCT_IDENTIFY = "product";
    public  static final String ALGORITHM_IDENTIFY = "processmodel";


    public  static final String TREE_IDENTIFY = "tree";
    public  static final String data_IDENTIFY = "data";

    /**
     * 产品勾选状态 (0非勾选|1勾选)
     */
    public  static final Integer PRODUCT_STATUS_ONE = 1;
    public  static final Integer PRODUCT_STATUS_ZERO = 0;
    public  static final Integer PRODUCT_SORT_KEY = 999;



    /**
     * 字典表 对应产品相关 TYPE信息
     */
    public  static final String PRODUCT_DATA_PATH_TYPE = "DATA_PATH";
    public  static final String PRODUCT_DATA_PATH_UPLOAD = "DATA_PATH_UPLOAD";
    public  static final String PRODUCT_DATA_PATH_DOWNLOAD = "DATA_PATH_DOWNLOAD";
    public  static final String PRODUCT_DATA_PATH_BASE = "DATA_PATH_BASE";

    /**
     * 树结构根节点id
     */
    public  static final String PRODUCT_BASE_ID = "0";
}
