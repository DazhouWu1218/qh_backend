package com.njht.webyun.management.upload.from;

import java.io.Serializable;

/**
 * 文件上传流程的枚举类
 * @author zhushizhen
 */

public enum UploadVersionEnum implements Serializable {
    YZ(0,"yanzheng"),
    CS(1,"chuanshu"),
    JX(2,"jiexi"),
    RK(3,"ruku");

    private int code;
    private String className;

    UploadVersionEnum() {
    }

    UploadVersionEnum(int code, String className) {
        this.code = code;
        this.className = className;
    }

    public int getCode() {
        return code;
    }


    public String getClassName() {
        return className;
    }
    public static String getUploadVersion(int code) {
        String className = "";
        for (UploadVersionEnum e : UploadVersionEnum.values()) {
            if (e.code==code) {
                className = e.className;
                break;
            }
        }
        return className;
    }
}
