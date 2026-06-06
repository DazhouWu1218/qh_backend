package com.njht.webyun.management.common.util;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;


public class GeoFormatUtil {
	public static String wkt2GeoJson(String wkt) {

        String ret = null;
        WKTReader reader = new WKTReader();
        try {
            Geometry geometry = reader.read(wkt);
            StringWriter writer = new StringWriter();
            GeometryJSON g = new GeometryJSON(12);
            g.write(geometry, writer);
            ret = writer.toString();
        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
        }
        return ret;
    }
	public static String geoJson2Wkt(String geoJson) {
        String ret = null;
        GeometryJSON gjson = new GeometryJSON();
        Reader reader = new StringReader(geoJson);
        try {
            Geometry geometry = gjson.read(reader);
            ret = geometry.toText();
        } catch (IOException e) {
//            LOGGER.error(e.getMessage(), e);
        }
        return ret;
    }
	
	public static void main(String[] args) {
		String gs = "{\"type\":\"Polygon\",\"coordinates\":[[[121.7,31.5547],[122.069,31.4851],[121.984,31.1548],[121.617,31.2243],[121.7,31.5547]]]}";
		String wkt = geoJson2Wkt(gs);
		System.out.println(wkt);
		System.out.println(wkt2GeoJson(wkt));
	}
}
