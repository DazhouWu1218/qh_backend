package com.njht.webyun.publish.behavior.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/29 16:23
 * @Description: 统计信息常量
 */
public class BehaviorConstant {

    private BehaviorConstant() { }

    /**
     *  0-用户访问量，1-数据下载量，2-用户注册量
     */
    public static final Integer ZERO = 0;
    public static final Integer ONE = 1;
    public static final Integer TWO = 2;
    public static final Integer THREE = 3;
    public static final Integer MINUS_ONE = -1;

    public static final String  STR_ZERO = "0";

    public static final String  STR_WEEK = "week";
    public static final String  STR_MON = "month";
    public static final String  STR_YEAR = "year";
    public static final String  STR_ALL = "ALL";

    public static final Map<Integer, String> ACTION_TYPE = new HashMap(3);
    static {
        ACTION_TYPE.put(0,"用户访问量");
        ACTION_TYPE.put(1,"数据下载量");
        ACTION_TYPE.put(2,"用户注册量");
    }
    public static final Map<Integer, String> ACTION_MAP = new HashMap(3);
    static {
        ACTION_MAP.put(0,"访问");
        ACTION_MAP.put(1,"下载");
        ACTION_MAP.put(2,"更改用户信息");
    }
}
