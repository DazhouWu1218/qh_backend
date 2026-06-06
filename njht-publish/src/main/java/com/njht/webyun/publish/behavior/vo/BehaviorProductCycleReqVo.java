package com.njht.webyun.publish.behavior.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/30 14:27
 * @Description: 产品饼状图信息
 */
@Data
public class BehaviorProductCycleReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private Long count;
}
