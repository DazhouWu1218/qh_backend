package com.njht.webyun.business.report.enums;

import java.util.Objects;

/**
 * 数据类型
 */
public enum ReportEnum {

    DATA_NON("0","数据未到"),
    DATA_ALL("1","数据完整"),
    DATA_MISS("2","数据缺失");

    public String getCode() {
        return code;
    }

    private String code;

    public String getDesc() {
        return desc;
    }

    private String desc;

    ReportEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String key) {
        for (ReportEnum st : ReportEnum.values()) {
            if (Objects.equals(key,st.code)) {
                return st.desc;
            }
        }
        return null;
    }
}
