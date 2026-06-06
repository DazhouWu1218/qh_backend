package com.njht.webyun.management.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dgj
 */
public class CycleType {

    /**
     *
     * COTM  Cycle of Ten Minute 10min周期
     * COOH  Cycle of One Hour	实时周期合成产品
     * COOD  Cycle of One Day	 日周期合成产品
     * COFD   Cycle of Five Day  侯周期合成产品
     * COSD	Cycle of Seven Days周周期合成产品
     * COOW Cycle OF a week    周产品
     * COTD	Cycle of Ten Days	 旬周期合成产品
     * COAM	Cycle of a Month	 月周期合成产品
     * COAQ	Cycle of a Quarter	 季周期合成产品
     * COAY	Cycle of a Year	 年周期合成产品
     */

    public static Map<String,String> cycleMap = new HashMap<>(16);
    static{
        cycleMap.put("COTM","10min");
        cycleMap.put("COOH","实时");
        cycleMap.put("COOD","日");
        cycleMap.put("COFD","候");
        cycleMap.put("COSD","周");
        cycleMap.put("COOW","周");
        cycleMap.put("COTD","旬");
        cycleMap.put("COAM","月");
        cycleMap.put("COAQ","季");
        cycleMap.put("COAY","年");
        cycleMap.put("buding","不定期");
        cycleMap.put("batian","八天");
        cycleMap.put("COED","八天");
    }

    public static Map<String,String> dataSourceMap = new HashMap<>(16);
    static{
        dataSourceMap.put("FirstEditionFusion","第一版融合");

    }
    public static Map<String,String> tifMap = new HashMap<>(16);
    static{
        tifMap.put("tif","栅格");
        tifMap.put("img","栅格");
        tifMap.put("nc","栅格");
        tifMap.put("shp","矢量");
    }
}
