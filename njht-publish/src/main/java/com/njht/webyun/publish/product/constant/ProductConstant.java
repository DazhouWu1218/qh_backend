package com.njht.webyun.publish.product.constant;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/25 13:47
 * @Description: 产品相关常量
 */
public class ProductConstant {

    private ProductConstant() {
    }

    public  static final String PRODUCT_TREE_KEY = "product";

    public  static final String PRODUCT_FILE_INFO_TYPE = "ALL";
    public  static final String PRODUCT_FILE_INFO_JPG = "jpg";
    public  static final String PRODUCT_FILE_INFO_PNG = "png";
    public  static final String PRODUCT_FILE_INFO_TIF = "tif";
    public  static final String PRODUCT_FILE_INFO_DOC = "doc";
    public  static final String PRODUCT_FILE_INFO_DOCX = "docx";
    public  static final String PRODUCT_FILE_INFO_GIF = ".gif";
    public  static final String PRODUCT_FILE_INFO_THUMB = "thumb";
    public  static final Integer PRODUCT_FILE_INFO_SHIER = 12;
    public  static final Integer PRODUCT_DEFAULT_ORG_ID = 95;



    public  static final String PRODUCT_TREE_NAME = "names";
    public  static final String DB_FIELD_FILE_TYPE = "file_type";
    public  static final String DB_FIELD_PRODUCT_INFO_ID = "product_info_id";
    public  static final Long PRODUCT_FILE_LENGTH = 1024L;

    public static final String PRODUCT_SEARCH_LOCK = "product_fuzzy_search_lock";
    public static final String PRODUCT_SEARCH_INFO = "fuzzySearchInfoList";
    public static final String PRODUCT_REDIS_SEARCH_INFO = "fuzzySearchInfoList::getFuzzySearchInfoList::";
}
