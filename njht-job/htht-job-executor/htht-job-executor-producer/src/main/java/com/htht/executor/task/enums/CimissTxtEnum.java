package com.htht.executor.task.enums;

/**
 * 目录树节点类型
 *     private final String txtCols_Grow = "crop_grow";
 *     private final String txtCols_Day = "surfFile";
 */
public enum CimissTxtEnum {

    txtCols_Grow("crop_grow","cimiss_agme_chn_crop_growth"),
    txtCols_Day("surfFile","cimiss_surf_chn_mul_day"),
    txtCole_Mon("surfFile","cimiss_surf_chn_mul_mon");

    private String desc;

    public String getDesc() {
        return desc;
    }

    private String tableName;
    public String getTableName() {
        return tableName;
    }

    CimissTxtEnum(String desc, String tableName) {
        this.desc = desc;
        this.tableName = tableName;
    }
}
