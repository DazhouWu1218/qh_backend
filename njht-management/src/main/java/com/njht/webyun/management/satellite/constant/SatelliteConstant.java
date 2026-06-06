package com.njht.webyun.management.satellite.constant;

import com.njht.webyun.management.satellite.vo.PngCoordinateInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 代国军
 * @CreateDate: 2022/2/11 16:22
 * @Description: 卫星数据相关常量定义
 */
public class SatelliteConstant {

    private SatelliteConstant() {}

    public static final String SATELLITE_MARK = "satellite-data";

    public static final Map<String, PngCoordinateInfo> regionInfoMap = new HashMap<>(16);
    static {
        regionInfoMap.put("QHS",new PngCoordinateInfo(89.112,31.4112,103.361,39.216));
        regionInfoMap.put("O10000",new PngCoordinateInfo(89.291297,31.438342,102.591037,37.218093));
    }

}
