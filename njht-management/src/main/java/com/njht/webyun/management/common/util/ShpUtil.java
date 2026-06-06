package com.njht.webyun.management.common.util;

import org.apache.commons.lang3.StringUtils;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author Administrator
 */
public class ShpUtil {


    public static SimpleFeatureCollection readShp(String path) {
        return readShp(path, null);
    }

    public static SimpleFeatureCollection readShp(String path, Filter filter) {
        SimpleFeatureSource featureSource = readStoreByShp(path);
        if (featureSource == null) {
            return null;
        }
        try {
            return filter != null ? featureSource.getFeatures(filter)
                    : featureSource.getFeatures();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SimpleFeatureSource readStoreByShp(String path) {
        File file = new File(path);
        FileDataStore store;
        SimpleFeatureSource featureSource = null;
        try {
            store = FileDataStoreFinder.getDataStore(file);
            ((ShapefileDataStore) store).setCharset(Charset
                    .forName("UTF-8"));
            featureSource = store.getFeatureSource();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return featureSource;
    }


    /**
     * 根据网格数据生成map文件
     */
    public static Map<String, List<String>> genFile(String shpPath) {

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        List<String> elements = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        // 读取shp
        SimpleFeatureCollection colls1 = readShp(shpPath);
        // 拿到所有features
        SimpleFeatureIterator iters = colls1.features();
        // 遍历打印
//		Map<String, MultiPolygon> map = new HashMap<String, MultiPolygon>();

        while (iters.hasNext()) {
            SimpleFeature sf = iters.next();
            if (elements.size() == 0) {
                Iterator<Property> it = sf.getProperties().iterator();
                while (it.hasNext()) {
                    Property per = it.next();
                    String key = per.getName().toString();
                    elements.add(key);
                }
            }
            List<Object> list = sf.getAttributes();
            for (int i = 0; i < list.size(); i++) {
                if (null == list.get(i) || list.get(i).toString().isEmpty()) {
                    list.set(i, "\'\'");
                }
            }
            values.add(StringUtils.join(list, ";"));
        }
        map.put("elements", elements);
        map.put("values", values);
        return map;
    }

    /**
     * 判断点面相交
     *
     * @param map
     * @param lat
     * @param lon
     * @return

    public static String getPloyonContainsPoint(Map<String, MultiPolygon> map,
    double lat, double lon) {

    GeometryFactory geometryFactory = new GeometryFactory();

    Coordinate coordinate = new Coordinate(lat, lon);
    com.vividsolutions.jts.geom.Geometry geo = geometryFactory
    .createPoint(coordinate);

    Set<String> set = map.keySet();

    for (String s : set) {
    MultiPolygon multiPolyon = map.get(s);
    if (multiPolyon.contains(geo)) {
    return s;
    }
    }
    return null;
    }
     */
    public static void main(String[] args) {
        String path1 = "d:/2018/ebeba5089a0947b2b284eeb141cd3ad4_cn.shp";


//		UploadShpFileServiceImpl up = new UploadShpFileServiceImpl();
//		up.shpFile2DB("bd", map.get("elements"), map.get("values"));

//        ShpUtil shpUtil = new ShpUtil();
//        String path1 = "C:/Users/admin/Desktop/生态红线/sp/HUMAN_BH.shp";
//        Map<String, List<String>> map = shpUtil.genFile1(path1);

    }

}


