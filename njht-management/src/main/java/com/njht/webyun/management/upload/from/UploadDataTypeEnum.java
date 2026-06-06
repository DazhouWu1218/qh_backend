package com.njht.webyun.management.upload.from;

/**
 * 文件上传的数据类型标识符
 * @author zhushizhen
 */

public enum UploadDataTypeEnum {

    TD("thematic-data","专题数据"),
    BD("basic-data","基础数据"),
    SAD("satellite-data","卫星数据"),
    SID("site-data","站点数据"),
    EK("grid-data","专家知识"),
    SERVICEK("service-product","服务产品");

    private String code;
    private String className;

    UploadDataTypeEnum() {
    }

    UploadDataTypeEnum(String code, String className) {
        this.code = code;
        this.className = className;
    }

    public String getCode() {
        return code;
    }


    public String getClassName() {
        return className;
    }
    public static String getUploadDataTypeEnum(String code) {
        String className = "";
        for (UploadDataTypeEnum e : UploadDataTypeEnum.values()) {
            if (e.code.equals(code)) {
                className = e.className;
                break;
            }
        }
        return className;
    }
}
