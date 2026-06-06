package com.htht.executor.task.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投影结果解析
 * @author yuguoqing
 * @Date 2018年10月23日 下午6:34:15
 *
 *
 */
public class XmlProjectionUtil {
	@SuppressWarnings("unchecked")
	private static List<Element> getRootNodes(String xmlPath) {
		SAXReader reader = new SAXReader();

		Document document = null;
		try {
			document = reader.read(xmlPath);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		if(null != document) {
			Element root = document.getRootElement();
			return root.elements();
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static Map<String, String> getProjection(String xmlPath) {
		List<Element> rootNodes = getRootNodes(xmlPath);

		Element et = null;
		Map<String, String> map = new HashMap<String, String>();
		// 预定义xml节点名与轨道对象方法名对应关系
		String[][] OrbitFields = {{"OrbitFilename", "setPath"},
				{"Satellite", "setSatellite"}, {"Sensor", "setSensor"},
				{"Level", "setDatalevel"}, {"DayOrNight", "setDayornight"},
				{"ObservationDate", "setObservationdate"},
				{"ObservationTime", "setObservationtime"},
				{"Station", "setStation"},
				{"OrbitIdentify", "setOrbitidentify"}};
		if(null != rootNodes){
			for (int i = 0; i < rootNodes.size(); i++) {
				et = (Element) rootNodes.get(i);
				// 循环依次得到子元素
				String ename = et.getName();
				if (ename.equals("OutputFiles")) {
					List<Element> subFileNode = et.elements();
					// 得到内层子节点
					if (subFileNode != null)
						for (int sj = 0; sj < subFileNode.size(); sj++) {
							List<Element> fileAttrNotes = ((Element) subFileNode
									.get(sj)).elements();
							if (fileAttrNotes != null && fileAttrNotes.size() > 0) {
								
								for (int noteIndex = 0; noteIndex < fileAttrNotes
										.size(); noteIndex++) {
									Element ele = (Element) fileAttrNotes
											.get(noteIndex);
									if ("OutputFilename".equals(ele.getName())) {
										map.put("OutputFilename",
												ele.getStringValue());
									} else if ("Thumbnail".equals(ele.getName())) {
										map.put("Thumbnail", ele.getStringValue());
									} else if ("ExtendFiles"
											.equals(ele.getName())) {
										map.put("ExtendFiles",
												ele.getStringValue());
									} else if ("ResolutionX"
											.equals(ele.getName())) {
										map.put("ResolutionX",
												ele.getStringValue());
									} else if ("ResolutionY"
											.equals(ele.getName())) {
										map.put("ResolutionY",
												ele.getStringValue());
									} else if ("Length".equals(ele.getName())) {
										map.put("ResolutionY", new BigDecimal(ele
												.getStringValue() == null
												|| "".equals(ele.getStringValue())
												? "0"
														: ele.getStringValue())
												+ "");
									} else if ("Envelope".equals(ele.getName())) {
//									String envelopeName = ele
//											.attributeValue("name");
										String envelopeminx = ele
												.attributeValue("minx");
										String envelopemaxx = ele
												.attributeValue("maxx");
										String envelopeminy = ele
												.attributeValue("miny");
										String envelopemaxy = ele
												.attributeValue("maxy");
										map.put("minx", envelopeminx);
										map.put("maxx", envelopemaxx);
										map.put("miny", envelopeminy);
										map.put("maxy", envelopemaxy);
									}
								}
								
							}
						}
				} else if (ename.equals("ProjectionIdentify")) {
					map.put("ProjectionIdentify", et.getStringValue());
				} else if (ename.equals("log")) {
					List<Element>  subLogNode = et.elements(); // 得到内层子节点
					map.put("loglevel",
							((Element) subLogNode.get(0)).getStringValue());
					map.put("loginfo",
							((Element) subLogNode.get(1)).getStringValue());
				} else {
					// 轨道数据信息
					for (@SuppressWarnings("unused") String[] f : OrbitFields) {
						if ("Sensor".equals(et.getName())) {
							map.put("ProjectionIdentify", et.getStringValue());
							break;
						}
						if ("Length".equals(et.getName())) {
							map.put("Length", et.getStringValue());
							break;
						}
						if ("DayOrNight".equals(et.getName())) {
							map.put("DayOrNight", et.getStringValue());
							break;
						}
						if ("Level".equals(et.getName())) {
							map.put("Level", et.getStringValue());
							break;
						}
					}
				}
				
			}
			return map;
		}
		return map;
	}
}
