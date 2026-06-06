package com.njht.webyun.management.order.entity;

import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/7/9 17:27
 * @Description: java类作用描述
 */
@Data
public class SateDataOrderEntity {

    private String id;
    private String satellite;
    private String originalPath;
    private String tifPath;
    private String datatype;
}
