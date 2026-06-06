package com.njht.webyun.management.order.vo;

import java.util.HashMap;
import java.util.Map;

public class TreeType {
    public static Map<String,String> treeMap = new HashMap<>(16);
    static{
        //草地类型
        treeMap.put("67cb313531d54d37b2814eaa6e8f8f8a","grass_land_type");
        //土壤容量
        treeMap.put("d280f6cbe18949c9b2ae2f5b74644c81","soil_land_type");
        //植被类型
        treeMap.put("84a531c92e1745e28c3f8975509c42a9","vegetation_land_type");
        //东部农区土地利用类型(详细)
        treeMap.put("f9123d059fb848b78af28bbdb9e625e0","nyq_land_use_type");
        //东部农区耕地范围
        treeMap.put("c0d176071cbc4752b2399efe83381253","nyq_cultivated_land");
        //土地利用类型-简单
        treeMap.put("231c7726abba453a9f687ed49030bea5","land_use_type_easy");
        //土地利用类型-详细
        treeMap.put("c2e24d99a589470a8ba1b3021ea445b6","land_use_type");
        //青海省高程
        treeMap.put("546a97ef32674ec3ad1bc6ba9e3a63ed","dem_qhs");
        //高速公路
        treeMap.put("d2cf5e4a79f24532ac81173063415d70","highway");
        //国道
        treeMap.put("8c66bfe70dc74f44b7733c13f132e190","NationalHighway");
        //省道
        treeMap.put("016ef0853c9c47f6952fec1858ead92e","ProvincialRoad");
        //铁路
        treeMap.put("85dff5cde23243f0a9da0f17f885925d","qh_tielu_al");
        //居民点
        treeMap.put("75ce6754bf294d87b1bd057157528c91","qh_jumdian_al");
        //流域范围
        treeMap.put("50c449970a7f4aa3b94c9bd65e09e57e","R10000,R20000,R30000,R40000,R50000,R51000,R52000,R53000,R54000,R55000,R56000,R60000,R70000,R80000");
        //冰川范围
        treeMap.put("1fd8515bb8674486bcf986a5f7b5f819","Glacier_QH");
        //河流水系
        treeMap.put("42455347d73d499893dec16da9d60a82","qh_hl_al");
        //省州县行政区划
        treeMap.put("3d46398eb92849d0971ffa835f49f04c","AreaProvince, AreaCounty, AreaCity");
        //生态功能区
        treeMap.put("33e7f10bc03f46b895491b95aa5ec7a4","O10000,O20000,O30000,O40000,O50000,O60000,O61000,O70000,O71000,O72000,O73000,O80000,O90000");
    }
}
