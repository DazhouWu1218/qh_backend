package com.njht.webyun.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.njht.entity.dataPush.HttpParamEntity;
import com.njht.webyun.spring.SpringContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MapUtil {

    private static MessageSource resource = (MessageSource) SpringContext.getBean("messageSource");
    public static String get(String key)
    {
        Locale locale = LocaleContextHolder.getLocale();
        return resource.getMessage(key,null,locale);
    }

    /**
     * 带参数
     * @param key
     * @param objs
     * @return
     */
    public static String get(String key,Object[] objs)
    {
        Locale locale = LocaleContextHolder.getLocale();
        return resource.getMessage(key,objs,locale);
    }

    public static String getParamString(Map<String, String[]> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(Map.Entry<String,String[]> entry:map.entrySet()){
            String key = entry.getKey();
            String value = printArray(entry.getValue());
            sb.append("[" + key + "=" + value + "]");
        }
        sb.append("}");
        return sb.toString();
    }


    public static String printArray(String[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static Map<String, Object> objectToMap(HttpParamEntity entity) {
        Map map = new HashMap(1);
        if (Objects.isNull(entity)) return map;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(entity);
            map = objectMapper.readValue(jsonString, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return map;
        }
        return map;
    }
}
