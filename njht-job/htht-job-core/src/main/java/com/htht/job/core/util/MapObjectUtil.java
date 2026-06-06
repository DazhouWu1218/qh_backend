package com.htht.job.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Map与Object对象转换工具
 * @author zedong
 */
public class MapObjectUtil {

    private MapObjectUtil(){}

    /**
     * map 转 java object 对象
     * @param map
     * @param tClass
     * @param <T>
     * @return
     */
    public static  <T> T mapToObject (Map map, Class<T> tClass){
        JSONObject jsonObject = new JSONObject();
        map = Objects.isNull(map)? new HashMap():map;
        jsonObject.putAll(map);
        T obj = JSON.toJavaObject(jsonObject,tClass);
        return obj;
    }

    /**
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> Map objectToMap(T obj ){
        if (Objects.isNull(obj)){
            return new HashMap();
        }
        String jsonString = JSON.toJSONString(obj);
        Map map = JSON.parseObject(jsonString,Map.class);
        return map;
    }
}
