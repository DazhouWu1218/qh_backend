package com.njht.webyun.enums;

/**
 * 目录树节点类型
 */
public enum TreeNodeEnum {

    CONTENT("0","目录"),
    DATA("1","数据");

    public String getCode() {
        return code;
    }

    private String code;

    public String getDesc() {
        return desc;
    }

    private String desc;

    TreeNodeEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
