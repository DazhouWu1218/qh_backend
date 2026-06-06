package com.njht.webyun.management.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 代国军
 * @CreateDate: 2021/8/31 19:33
 * @Description: 周期信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CycleTypeInfo {

    private String value;
    private Integer weights;
}
