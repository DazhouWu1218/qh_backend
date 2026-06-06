package com.htht.executor.task.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XmlMakeUtil {

	private static Logger logger = LoggerFactory.getLogger(XmlMakeUtil.class);
	
	public static boolean makeXml(Map<String, Object> argMap, String xmlFilePath) throws Exception {
		XmlMakeUtil util = new XmlMakeUtil();
		util.doMakeXml(argMap, xmlFilePath);
		return true;

	}

	/**
	 * 生成exe执行需要的xml
	 * 
	 * @param argMap
	 * @param xmlFilePath
	 * @throws Exception
	 */
	private void doMakeXml(Map<String, Object> argMap, String xmlFilePath){	
		try{
			String newFileContent = "";
			if (xmlFilePath.contains("FY4A-")) {
				newFileContent = getXmlContentFY4A(argMap);
			} else {
				newFileContent = getXmlContent(argMap);
			}
			File xmlFile = new File(xmlFilePath);
			xmlFile.deleteOnExit();
			if (!xmlFile.getParentFile().exists())
			{
				xmlFile.getParentFile().mkdirs();
			}
			xmlFile.createNewFile();
			try(
				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(xmlFile, false), "UTF-8");
				){
				osw.write(newFileContent);
			}
		}catch(Exception e) {
			logger.error(e.toString());
		}
	}

	/**
	 * 根据参数Map生成xml内容
	 * 
	 * @param argMap
	 * @return
	 */
	private String getXmlContent(Map<String, Object> argMap) throws Exception {

		String newFileContent = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r" + "<xml identify=\"projectionarg\">";
		Set<String> keySet = argMap.keySet();

		for (Iterator<String> iter = keySet.iterator(); iter.hasNext();)
		{
			String key = iter.next();
			Object value = argMap.get(key);
			String sLine = makeXmlLine(key, value);
			if (sLine != null && !sLine.equals(""))
				newFileContent += "\r\t" + sLine;
		}
		return newFileContent + "\r</xml>";
	}
	
	/**
	 * 根据参数Map生成xml内容(FY4A)
	 * 
	 * @param argMap
	 * @return
	 */
	private String getXmlContentFY4A(Map<String, Object> argMap) throws Exception {

		String newFileContent = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r" + "<xml>";
		Set<String> keySet = argMap.keySet();

		for (Iterator<String> iter = keySet.iterator(); iter.hasNext();) {
			String key = iter.next();
			Object value = argMap.get(key);
			String sLine = makeXmlLine(key, value);
			if (sLine != null && !sLine.equals(""))
				newFileContent += "\r\t" + sLine;
		}
		return newFileContent + "\r</xml>";
	}

	/**
	 * 根据单个参数创建一个DOM节点
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	private String makeXmlLine(final String key, Object value) throws Exception {

		String newLineStr = "";

		if (value == null || "none".equals(value) || "".equals(value)) {
			newLineStr += "<" + key + "></" + key + ">";
		} else if (value instanceof String) {
			newLineStr += "<" + key + ">" + value + "</" + key + ">";
		} else if (value instanceof List)
		{

		} else if (value instanceof Map) {
			if ("OutputFiles".equals(key)) {
				newLineStr +="<" + key + ">" + "\r\t" + "<File>";
			} else {
				newLineStr +="<" + key + ">";
			}
			Set<String> keySet = ((Map<String, Object>) value).keySet();

			for (Iterator<String> iter = keySet.iterator(); iter.hasNext();)
			{
				String newkey = iter.next();
				Object newvalue = ((Map<String, Object>) value).get(newkey);
				String sLine = makeXmlLine(newkey, newvalue);
				if (sLine != null && !sLine.equals(""))
					newLineStr += "\r\t" + sLine;
			}
			if ("OutputFiles".equals(key)) {
				newLineStr += "\r\t"+ "</File>" + "\r\t" + "</" + key + ">";
			} else {
				newLineStr += "\r\t" + "</" + key + ">";
			}
		} else if (value instanceof String[]) {
			if ("ValidEnvelopes".equals(key)||"Envelopes".equals(key)) {
				newLineStr += "<" + key + ">";
				newLineStr += makeEnvelopes((String[]) value);
				newLineStr += "</" + key + ">";
			} else {
				newLineStr += makeEnvelopes((String[]) value);
			}
		}
		return newLineStr;
	}

	private String makeEnvelopes(String[] estrs) throws Exception {
		String retStr = "";
		if (estrs == null)
		{
			return "";
		}
		for (String estr : estrs)
		{
			if (estr == null || "".equals(estr))
			{
				continue;
			}
			retStr += "<Envelope ";
			estr = estr.replaceAll("\\s+", "");
			String attrs[] = estr.split(",");
			for (String attr : attrs)
			{
				String a[] = attr.split(":");
				retStr += " " + a[0] + "=\"" + a[1] + "\"";
			}
			retStr += "/>";
		}
		return retStr;
	}
	
}
