package com.njht.webyun.utils;


import com.alibaba.fastjson.JSON;

/**
 * @Author chengqh
 * @Date 2020/11/19
 */
public class JsonUtil {
    public static String toJson(Object object) {
        return JSON.toJSONString(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }


}
