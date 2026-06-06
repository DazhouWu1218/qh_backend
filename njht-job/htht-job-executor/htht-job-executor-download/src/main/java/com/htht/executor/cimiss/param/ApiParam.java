package com.htht.executor.cimiss.param;

import lombok.Data;

import java.util.HashMap;

/**
 * @author daiguojun
 * @date 2022-08-09 9:44
 * 接口调用参数
 */
@Data
public class ApiParam {

    private String userId;
    private String pwd;
    private String interfaceId;
    private HashMap<String, String> params;
    private String dataFormat;
    private StringBuffer retStr;
}
