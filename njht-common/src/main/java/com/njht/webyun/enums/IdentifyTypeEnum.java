package com.njht.webyun.enums;

import java.util.Objects;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/18 15:53
 * @Description: 标识信息枚举类
 */
public enum IdentifyTypeEnum {

    BUSINESS_PRODUCT("product","产品生产",3),
    PRE_DATA("preprocess","数据预处理",2),
    DATA_COLLECTION("dataCollection","数据汇集",1);

    private String key;
    private String value;
    private Integer sort;

    IdentifyTypeEnum(String key, String value, Integer sort) {
        this.key = key;
        this.value = value;
        this.sort = sort;
    }

    public static String getValue(String key) {
        for (IdentifyTypeEnum st : IdentifyTypeEnum.values()) if (Objects.equals(key, st.key)) return st.value;
        return "";
    }

    public static Integer getSort(String key) {
        for (IdentifyTypeEnum st : IdentifyTypeEnum.values()) if (Objects.equals(key, st.key)) return st.sort;
        return 99;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSort() {
        return sort;
    }

    public IdentifyTypeEnum setSort(Integer sort) {
        this.sort = sort;
        return this;
    }
}
