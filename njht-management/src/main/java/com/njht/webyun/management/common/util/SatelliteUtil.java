package com.njht.webyun.management.common.util;

/**
 * @Author: 代国军
 * @CreateDate: 2021/7/20 14:30
 * @Description: 卫星相关工具类
 */
public class SatelliteUtil {

    public static final  String LOW_RESOLUTION_SATE = "basis_low";
    public static final  String HIGH_RESOLUTION_SATE = "basis_high";

    public static final  String LRS = "FY3B,FY4A,FY3C,FY3D,H8,TERRA,AQUA,NOAA18,NOAA19,Suomi/NPP";
    public static final  String HRS = "GF1,GF2,GF6,HJ1A,HJ1B,Sentinel1,Sentinel2,Sentinel3";

    public static String getSatelliteStatue(String satelliteId) {
        if(LRS.contains(satelliteId)){
            return LOW_RESOLUTION_SATE;
        }else if(HRS.contains(satelliteId)){
            return HIGH_RESOLUTION_SATE;
        }else {
            return null;
        }
    }
}
