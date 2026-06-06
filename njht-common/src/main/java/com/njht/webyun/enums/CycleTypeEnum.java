package com.njht.webyun.enums;

import java.util.Objects;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/18 15:53
 * @Description: 周期信息枚举类
 */
public enum  CycleTypeEnum {
    
    COTM("COTM","10min",11),
    COOH("COOH","实时",10),
    COOD("COOD","日",9),
    COFD("COFD","候",8),
//    COSD("COSD","周",7),
    COOW("COOW","周",6),
    COTD("COTD","旬",4),
    COAM("COAM","月",3),
    COAQ("COAQ","季",2),
    COAY("COAY","年",1),
    COED("COED","八天",5);

    private String key;
    private String value;
    private Integer sort;

    private CycleTypeEnum(String key, String value,Integer sort) {
        this.key = key;
        this.value = value;
        this.sort = sort;
    }

    public static String getValue(String key) {
        for (CycleTypeEnum st : CycleTypeEnum.values()) {
            if (Objects.equals(key,st.key)) {
                return st.value;
            }
        }
        return "";
    }

    public static Integer getSort(String key) {
        for (CycleTypeEnum st : CycleTypeEnum.values()) {
            if (Objects.equals(key,st.key)) {
                return st.sort;
            }
        }
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

    public CycleTypeEnum setSort(Integer sort) {
        this.sort = sort;
        return this;
    }
}
