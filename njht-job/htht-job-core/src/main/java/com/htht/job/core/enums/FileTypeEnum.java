package com.htht.job.core.enums;

import lombok.AllArgsConstructor;

/**
 * 文件类型枚举类
 */
@AllArgsConstructor
public enum FileTypeEnum {

    XLS("xls","excel表格");

    private String code;

    private String desc;

    public String getCode (){
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
