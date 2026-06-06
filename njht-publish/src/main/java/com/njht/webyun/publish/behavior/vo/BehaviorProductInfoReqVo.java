package com.njht.webyun.publish.behavior.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/30 14:27
 * @Description: 产品基本信息
 */
@Data
public class BehaviorProductInfoReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private List<Integer> yList;
}
