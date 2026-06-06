package com.njht.webyun.management.business.entity;

import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/6/2 9:54
 * @Description: java类作用描述
 */
@Data
public class UnitInfo {

    private String unit = "℃";
    private String max = "100";
    private String min = "0";
    private String isAno;
    private String mark;
}
