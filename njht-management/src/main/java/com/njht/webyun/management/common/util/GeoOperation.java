package com.njht.webyun.management.common.util;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.WKTReader;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * gemotry之间的关系分析
 *
 * @author zzp
 */
public class GeoOperation {

	private GeometryFactory geometryFactory = new GeometryFactory();

	/**
	 * create a Point
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public Coordinate point(double x, double y) {
		return new Coordinate(x, y);
	}

	/**
	 * create a line
	 *
	 * @return
	 */
	public LineString createLine(List<Coordinate> points) {
		Coordinate[] coords = (Coordinate[]) points
				.toArray(new Coordinate[points.size()]);
		LineString line = geometryFactory.createLineString(coords);
		return line;
	}

	/**
	 * 返回a指定距离内的多边形和多多边形
	 *
	 * @param a
	 * @param distance
	 * @return
	 */
	public Geometry bufferGeo(Geometry a, double distance) {
		return a.buffer(distance);
	}

	/**
	 * 返回(A)与(B)中距离最近的两个点的距离
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public double distanceGeo(Geometry a, Geometry b) {
		return a.distance(b);
	}

	/**
	 * 两个几何对象的交集
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public Geometry intersectionGeo(Geometry a, Geometry b) {
		return a.intersection(b);
	}

	/**
	 * 几何对象合并
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public Geometry unionGeo(Geometry a, Geometry b) {
		return a.union(b);
	}

	/**
	 * 在A几何对象中有的，但是B几何对象中没有
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public Geometry differenceGeo(Geometry a, Geometry b) {
		return a.difference(b);
	}

	/**
	 * 计算多个几何对象的并集
	 */
	public static Geometry combineIntoOneGeometry(
			Collection<Geometry> geometryCollection) {
		Geometry all = null;
		for (Iterator<Geometry> i = geometryCollection.iterator(); i.hasNext();) {
			Geometry geometry = i.next();
			if (geometry == null){
				continue;
			}
			if (all == null) {
				all = geometry;
			} else {
				all = all.union(geometry);
			}
		}
		return all;
	}

	/**
	 * A几何对象占B几何对象的百分比
	 *
	 * @param a 检索结果的并集对象
	 * @param b 前端传的空间条件对象
	 * @return
	 */
	public static String percentage(Geometry a, Geometry b){
		Geometry b1 = b.difference(a);
		NumberFormat numberFormat=NumberFormat.getPercentInstance();
		numberFormat.setMinimumFractionDigits(2);
		String result=numberFormat.format(1-b1.getArea()/b.getArea());
		return result;
	}

	public static void main(String[] args) throws Exception {
		/*
		 * 获取外接多边形，本项目中主要用于规范入库时坐标的四角顺序
		Geometry g = g.convexHull();
		 */
		/*
		 * 判断是否相交
		String s1= "{\"type\":\"Polygon\",\"coordinates\":[[[0,0],[0,1],[1,1],[1,0],[0,0]]]}";
		String s2= "{\"type\":\"Polygon\",\"coordinates\":[[[0,0],[3,4],[4,4],[4,3],[0,0]]]}";
		String s3= "{\"type\":\"Polygon\",\"coordinates\":[[[3,3],[3,4],[4,4],[4,3],[3,3]]]}";
		 Geometry g1 = new WKTReader().read(GeoFormatUtil.geoJson2Wkt(s1));
		 Geometry g2 = new WKTReader().read(GeoFormatUtil.geoJson2Wkt(s2));
		 Geometry g3 = new WKTReader().read(GeoFormatUtil.geoJson2Wkt(s3));
		 boolean b = g1.intersects(g2);
		 boolean b1 = g1.intersects(g3);
		 System.out.println(b);
		 System.out.println(b1);
		*/
		/*计算并集
		String c1 = "POLYGON ((121.188650141 31.0071923843, 121.321392227 30.9741045603, 121.286139271 30.8358120807, 121.15365295 30.8690278686, 121.188650141 31.0071923843))";
		String c2 = "POLYGON ((121.188650141 31.0071923843, 121.321392227 30.9741045603, 121.15365295 30.8690278686, 121.188650141 31.0071923843))";
//		String c2 = "POLYGON ((120.791071 31.741981,121.317715 31.647811,120.685058 31.301024,121.209192 31.207065,120.791071 31.741981))";
//		String c1 = "POLYGON ((120.791071 31.741981,121.317715 31.647811,120.685058 31.301024,121.209192 31.207065,120.791071 31.741981))";
		 Geometry gc1 = new WKTReader().read(c1);
		 gc1.setSRID(4326);
		 Geometry gc2 = new WKTReader().read(c2);
		 gc2.setSRID(4326);
		 List<Geometry> list = new ArrayList<Geometry>();
		 list.add(gc2);
		 list.add(gc1);
		 Geometry g4 = combineIntoOneGeometry(list);
		 System.out.println(GeoFormatUtil.wkt2GeoJson(g4.toText()));
		 */
		String s1= "{\"type\":\"Polygon\",\"coordinates\":[[[114.599238,36.372266],[114.484936,36.392415],[114.459531,36.297442],[114.573693,36.277305],[114.599238,36.372266]]]}";
		String s2 ="{\"type\":\"Polygon\",\"coordinates\":[[[114.704,36.3283],[114.578,36.352],[114.549,36.2512],[114.674,36.2275],[114.704,36.3283]]]}";
		String s3 ="{\"type\":\"Polygon\",\"coordinates\":[[[114.73,36.419],[114.605,36.4427],[114.575,36.3419],[114.701,36.3181],[114.73,36.419]]]}";
		 Geometry g1 = new WKTReader().read(GeoFormatUtil.geoJson2Wkt(s1));
		 Geometry g2 = new WKTReader().read(GeoFormatUtil.geoJson2Wkt(s2));
		 Geometry g3 = new WKTReader().read(GeoFormatUtil.geoJson2Wkt(s3));
		 List<Geometry> list = new ArrayList<Geometry>();
		 list.add(g2);
		 list.add(g1);
		 list.add(g3);
		 Geometry g4 = combineIntoOneGeometry(list);
		 System.out.println(GeoFormatUtil.wkt2GeoJson(g4.toText()));
	
	}
}