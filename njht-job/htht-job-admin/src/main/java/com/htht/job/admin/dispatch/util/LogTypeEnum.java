package com.htht.job.admin.dispatch.util;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/18 15:53
 * @Description: 日志类型枚举
 */
public enum LogTypeEnum {

    LOG_NUMBER_1(1,"清理一个月之前日志数据"),
    LOG_NUMBER_2(2,"清理三个月之前日志数据"),
    LOG_NUMBER_3(3,"清理六个月之前日志数据"),
    LOG_NUMBER_4(4,"清理一年之前日志数据"),
    LOG_NUMBER_5(5,"清理一千条以前日志数据"),
    LOG_NUMBER_6(6,"清理一万条以前日志数据"),
    LOG_NUMBER_7(7,"清理三万条以前日志数据"),
    LOG_NUMBER_8(8,"清理十万条以前日志数据"),
    LOG_NUMBER_9(9,"清理所有日志数据");

    private int key;
    private String value;

    LogTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
