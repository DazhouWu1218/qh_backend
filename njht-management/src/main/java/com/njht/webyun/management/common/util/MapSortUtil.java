package com.njht.webyun.management.common.util;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @Author: 代国军
 * @CreateDate: 2021/6/11 13:30
 * @Description: map排序工具类
 */
public class MapSortUtil {
    /**
     * map 根据value排序
     * @param tableData
     * @param identify
     * @return
     */
    public static List<Map<String, Object>> sortByMapValue(List<Map<String, Object>> tableData, String identify) {
        Collections.sort(tableData, (o1, o2) -> {
            BigDecimal name1 =(BigDecimal)o1.get(identify);
            BigDecimal name2= (BigDecimal)o2.get(identify);
            return name2.compareTo(name1);
        });
        return tableData;
    }

    /**
     * map 按 key 升序排序
     */
    public static Map<String, BigDecimal> sortByKey(Map<String, BigDecimal> map) {
        Map<String, BigDecimal> result = new LinkedHashMap<>(map.size());
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    /**
     * map 按 key 升序排序
     */
    public static Map<String, String> sortByKey1(Map<String, String> map) {
        Map<String, String> result = new LinkedHashMap<>(map.size());
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }


    /**
     * list集合去重
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }



}
