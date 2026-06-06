package com.njht.webyun.management.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 代国军
 * @CreateDate: 2021/4/29 19:15
 * @Description: 专题数据下载相关
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderListInfo {

    private String dataId;
    private String time;
    private String dataType;


}
