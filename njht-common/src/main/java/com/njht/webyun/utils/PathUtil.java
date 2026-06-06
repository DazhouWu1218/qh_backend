package com.njht.webyun.utils;

import org.springframework.util.AntPathMatcher;

/**
 * @Author chengqh
 * @Date 2020/11/18
 */
public class PathUtil {

    private static AntPathMatcher matcher = new AntPathMatcher();

    public static boolean isPathMatch(String pattern, String path) {
        return matcher.match(pattern, path);
    }
}
