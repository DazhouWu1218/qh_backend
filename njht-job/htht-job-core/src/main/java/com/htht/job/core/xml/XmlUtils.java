package com.htht.job.core.xml;

import com.alibaba.fastjson.JSONObject;
import com.htht.job.core.entity.xml.XmlDTO;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;

public class XmlUtils {

  private static ThreadLocal<Map<String,List<Element>>> resLocal = new ThreadLocal<Map<String,List<Element>>>(){

		@Override
		protected Map<String, List<Element>> initialValue() {
			return new HashMap<>();
		}
		
  };
  
  private static ThreadLocal<List<String>> listsLocal = new ThreadLocal<List<String>>(){

		@Override
		protected List<String> initialValue() {
			return new ArrayList<>();
		}
  	
  };
  
  public static List<String> getListsLocal(){
  	return listsLocal.get();
  }
  
  public static void removeLocal(){
  	listsLocal.remove();
  	resLocal.remove();
  }

  public static boolean createAlgorithmXml(String rootTag, List<XmlDTO> inputList, List<XmlDTO> outputList, String path) {

      //判断文件是否存在，不存在就创建
      File file = new File(path);

      File fileParent = file.getParentFile();
      if(!fileParent.exists()){
          fileParent.mkdirs();
      }

      if (!file.exists()){
          try {
              file.createNewFile();
          } catch (IOException e) {
              e.printStackTrace();
              return false;
          }
      }

      // 创建文档对象
      Document doc = DocumentHelper.createDocument();
      // 创建根节点
      Element root = doc.addElement("xml");
      //根节点添加identify属性
      if(!StringUtils.isEmpty(rootTag)){
      	root.addAttribute("identify", rootTag);
      }
      //从inputList添加input节点
      for (int i = 0; i < inputList.size(); i++) {
          // 创建input节点
          Element inputElement = root.addElement("input");
          //赋值
          if(!StringUtils.isEmpty( inputList.get(i).getIdentify())){
          	inputElement.addAttribute("identify", inputList.get(i).getIdentify());
          }
          if(!StringUtils.isEmpty(inputList.get(i).getType())){
          	inputElement.addAttribute("type", inputList.get(i).getType());
          }
          if(!StringUtils.isEmpty(inputList.get(i).getDescription())){
          	inputElement.addAttribute("des", inputList.get(i).getDescription());
          }
          if(!StringUtils.isEmpty(inputList.get(i).getValue())){
              inputElement.setText(inputList.get(i).getValue());
          }
      }


      //从outputList添加outputs节点
      for (int i = 0; i < outputList.size(); i++) {
          // 创建input节点
          Element inputElement = root.addElement("output");
          //赋值
          if(!StringUtils.isEmpty( outputList.get(i).getIdentify())){
          	inputElement.addAttribute("identify", outputList.get(i).getIdentify());
          }
          if(!StringUtils.isEmpty(outputList.get(i).getType())){
          	inputElement.addAttribute("type", outputList.get(i).getType());
          }
          if(!StringUtils.isEmpty(outputList.get(i).getDescription())){
          	inputElement.addAttribute("des", outputList.get(i).getDescription());
          }
          if(!StringUtils.isEmpty(outputList.get(i).getValue())){
              inputElement.setText(outputList.get(i).getValue());
          }
      }

      // 设置XML文档格式
      OutputFormat outputFormat = OutputFormat.createPrettyPrint();
      // 设置XML编码方式,即是用指定的编码方式保存XML文档到字符串(String),这里也可以指定为GBK或是ISO8859-1
      outputFormat.setEncoding("UTF-8");
      // outputFormat.setSuppressDeclaration(true); //是否生产xml头
      outputFormat.setIndent(true); // 设置是否缩进
      outputFormat.setIndent("    "); // 以四个空格方式实现缩进
      outputFormat.setNewlines(true); // 设置是否换行
      XMLWriter writer = null;
      try {
          writer = new XMLWriter(outputFormat);
      } catch (UnsupportedEncodingException e1) {
          e1.printStackTrace();
      }
      try {
          FileOutputStream fos = new FileOutputStream(path);
          assert writer != null;
          writer.setOutputStream(fos);
          writer.write(doc);
          writer.close();
          System.out.println("写入完毕！文件位置：" + path);
          return true;
      } catch (IOException e) {
          e.printStackTrace();
          return false;
      }
  }


  public static Map<String,List<Element>> xmlToMap(String path) {
      //清空res map
  	resLocal.get().clear();
      //创建SAXReader对象
      SAXReader reader = new SAXReader();
      Document document = null;
      try {
          document = reader.read(new File(path));
          //获取文档根节点
          Element root = document.getRootElement();
          //调用下面获取子节点的递归函数。
          getChildNodes(root);
          return resLocal.get();
      } catch (DocumentException e) {
          e.printStackTrace();
          return resLocal.get();
      }
  }

  public static Map<String,List<Element>> elementXmlToMap(String path, String elementName) {
      //清空res map
      resLocal.get().clear();
      //创建SAXReader对象
      SAXReader reader = new SAXReader();
      Document document = null;
      return getStringObjectMap(path, reader, elementName);
  }

  public static Map<String,List<Element>> outputFilesXmlToMap(String path) {
      //清空res map
      resLocal.get().clear();
      //创建SAXReader对象
      SAXReader reader = new SAXReader();
      Document document = null;
      return getStringObjectMap(path, reader, "outFiles");
  }

  private static Map<String,List<Element>> getStringObjectMap(String path, SAXReader reader, String outputfiles) {
      Document document;
      try {
          document = reader.read(new File(path));
          //获取文档outputfiles节点
          Element outputFilesElem = document.getRootElement().element(outputfiles);

          //调用下面获取子节点的递归函数。
          getChildNodes(outputFilesElem);
          return resLocal.get();
      } catch (DocumentException e) {
          e.printStackTrace();
          return resLocal.get();
      }
  }

  public static Map<String,List<Element>> tablesXmlToMap(String path) {
      //清空res map
      resLocal.get().clear();
      //创建SAXReader对象
      SAXReader reader = new SAXReader();
      Document document = null;
      return getStringObjectMap(path, reader, "tables");
  }

  public static Map<String, Object> getProductXmlStatus(String path) {
      Map<String, Object> res = new HashMap<>();
      //创建SAXReader对象
      SAXReader reader = new SAXReader();
      Document document = null;
      try {
          document = reader.read(new File(path));
          //获取文档根节点
          Element root = document.getRootElement();
          Element statusElem = root.element("log").element("status");
          Element infoElem = root.element("log").element("info");
          if(!StringUtils.isEmpty(statusElem)){
              res.put("status", statusElem.getText());
          }
          if(!StringUtils.isEmpty(infoElem)){
              res.put("info", infoElem.getText());
          }
          return res;
      } catch (DocumentException e) {
          e.printStackTrace();
          return res;
      }
  }

  public static boolean isSuccessByXml(String path) {
      //创建SAXReader对象
      SAXReader reader = new SAXReader();
      Document document = null;
      try {
          document = reader.read(new File(path));
          //获取文档根节点
          Element root = document.getRootElement();
          Element statusElem = root.element("log").element("status");
          if("1".equals(statusElem.getText())){
              return true;
          }else {
              return false;
          }
      } catch (DocumentException e) {
          e.printStackTrace();
          return false;
      }
  }
  
  @SuppressWarnings("finally")
	public static String getFailedInfo(String path) {
      //创建SAXReader对象
      SAXReader reader = new SAXReader();
      Document document = null;
      String errMsg = "failed!";
      try {
			document = reader.read(new File(path));
			//获取文档根节点
			Element root = document.getRootElement();
			Element statusElem = root.element("log").element("info");
			if ("".equals(statusElem.getText())) {
			    return errMsg;
			}
			errMsg = statusElem.getText();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			return errMsg;
		}

  }

  public static List<String >getXmlAttrVal(String path, String elementName , String attrName) {
  	Map<String,List<Element>> map = outputFilesXmlToMap(path);
  	List<String> eList = new ArrayList<>();
  	List<Element> list = map.get(elementName);
  	if(list!=null&&list.size()>0) {
  		for(Element e:list) {
  			eList.add(e.attribute(attrName).getValue().trim());
  		}
  	}
  	return eList;
  }
  public static List<String >getXmlAttrVal(Map<String,List<Element>> map, String elementName , String attrName) {
  	List<String> eList = new ArrayList<>();
  	List<Element> list = map.get(elementName);
  	if(list!=null&&list.size()>0) {
  		for(Element e:list) {
  			eList.add(e.attribute(attrName).getValue().trim());
  		}
  	}
  	return eList;
  }

  public static List<String> getXmlAttrFileElementVal(String path, String elementName,String identify,String regionId) {
  	Map<String,List<Element>> map = outputFilesXmlToMap(path);
  	List<Element> elementList = map.get(elementName);
  	if(elementList!=null&&elementList.size()>0) {
  		for(Element element:elementList) {
  			if(element.attributeValue(identify).trim().equalsIgnoreCase(regionId)) {
  				listsLocal.get().clear();
  			        getElementsFileVal(element);
  			}
  		}
  	}
      return listsLocal.get();
  }
  public static List<String> getXmlAttrFileElementVal(Map<String,List<Element>> map, String elementName,String identify,String regionId) {
  	List<Element> elementList = map.get(elementName);
  	if(elementList!=null&&elementList.size()>0) {
  		for(Element element:elementList) {
  			if(element.attributeValue(identify).trim().equalsIgnoreCase(regionId)) {
  				listsLocal.get().clear();
  				getElementsFileVal(element);
  			}
  		}
  	}
  	return listsLocal.get();
  }
  
  public static List<String> getXmlAttrFileElementVal(String path, String elementName) {
  	Map<String,List<Element>> map = outputFilesXmlToMap(path);
      Element element = map.get(elementName).get(0);
      listsLocal.get().clear();
      getElementsFileVal(element);
      return listsLocal.get();
  }
  public static List<String> getXmlAttrFileElementVal(Map<String,List<Element>> map, String elementName) {
  	Element element = map.get(elementName).get(0);
  	listsLocal.get().clear();
  	getElementsFileVal(element);
  	return listsLocal.get();
  }
  
  public static List<Element> getXmlElements(String path, String elementName) {
  	Map<String,List<Element>> map = outputFilesXmlToMap(path);
      return map.get(elementName);
  }

  @SuppressWarnings("finally")
	public static List<Element> getTablenameElements(String path,String tableName) {
      List<Element> lists = new ArrayList<>();
      List res = new ArrayList();
		try {
			res = (List)tablesXmlToMap(path).get(tableName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (null == res || 0 == res.size()) {
				 return lists;
			}
			if(res.size() > 0){
	            for (int i = 0; i < res.size(); i++) {
	                lists.add((Element)res.get(i));
	            }
	        }
			 return lists;
		}
  }

  //递归查询节点函数,输出节点名称
  private static void getChildNodes(Element elem){
      if(resLocal.get().containsKey(elem.getName())){
      	resLocal.get().get(elem.getName()).add(elem);
      }else{
      	List<Element> list = new ArrayList<>();
      	list.add(elem);
          resLocal.get().put(elem.getName(),list);
      }
      Iterator<Node> it = elem.nodeIterator();
      while (it.hasNext()){
          Node node = it.next();
          if (node instanceof Element){
              Element e1 = (Element)node;
              getChildNodes(e1);
          }
      }
  }

  //递归查询节点
  private static void getElementsFileVal(Element elem){
      Iterator<Node> it = elem.nodeIterator();
      while (it.hasNext()){
          Node node = it.next();
          if (node instanceof Element){
              Element e1 = (Element)node;
              if("file".equals(e1.getQName().getName())){
                  listsLocal.get().add(e1.getText());
              }
              getChildNodes(e1);
          }
      }
  }

  public static List<String> getFileElements(String path, String elementName) {
      listsLocal.get().clear();
      Map<String,List<Element>> m = xmlToMap(path);
      Element e =  m.get(elementName).get(0);
      getNodes(e);
      return listsLocal.get();
  }

  private static void getNodes(Element node){
      if("file".equals(node.getName())){
          listsLocal.get().add(node.getTextTrim());
      }
      //递归遍历当前节点所有的子节点
      List<Element> listElement=node.elements();//所有一级子节点的list
      for(Element e:listElement){//遍历所有一级子节点
          getNodes(e);//递归
      }
  }


    public static int isFireSuccessByXml(String path) {
        //创建SAXReader对象
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new File(path));
            //获取文档根节点
            Element root = document.getRootElement();
            Element statusElem = root.element("log").element("status");
            Element infoElem = root.element("log").element("info");
            if("0".equals(statusElem.getText())){
                return 0;
            }else if("1".equals(statusElem.getText()) && infoElem.getText().toUpperCase().indexOf("SUCCESS")>-1){
                return 1;
            }else if("2".equals(statusElem.getText()) && infoElem.getText().toUpperCase().indexOf("SUCCESS")>-1){
                return 2;
            }
        } catch (DocumentException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    /**
     * xml 字符串 转为 json对象
     * @param xml
     * @return
     */
    public static JSONObject xmlToJsonObject(String xml){
        if (org.apache.commons.lang3.StringUtils.isNotBlank(xml)){
            try {
                JSONObject json = new JSONObject();
                InputStream is = new ByteArrayInputStream(xml.getBytes());
                SAXBuilder sb = new SAXBuilder();
                org.jdom.Document doc = sb.build(is);
                org.jdom.Element root = doc.getRootElement();
                json.put(root.getName(), iterateElement(root));
                if (json.size()> 0){
                    return json;
                }
            } catch (JDOMException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * 迭代xml内容
     * @param element
     * @return
     */
    public static JSONObject iterateElement(org.jdom.Element element) {
        List node = element.getChildren();
        org.jdom.Element et = null;
        JSONObject obj = new JSONObject();
        List list = null;
        for (int i = 0; i < node.size(); i++) {
            list = new LinkedList();
            et = (org.jdom.Element) node.get(i);
            if (et.getTextTrim().equals("")) {
                if (et.getChildren().size() == 0)
                    continue;
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(iterateElement(et));
                obj.put(et.getName(), list);
            } else {
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(et.getTextTrim());
                obj.put(et.getName(), list);
            }
        }
        return obj;
    }

    /**
     * 根据map生成xml元素集合
     * @param map
     * @return
     */
    public static List<XmlDTO> formatXmlParam(Map<String, Object> map) {
        Set<String> keys = map.keySet();
        List<XmlDTO> list = new ArrayList<>();
        for (String key : keys) {
            XmlDTO xmlDTO = new XmlDTO();
            xmlDTO.setIdentify(key);
            xmlDTO.setValue(String.valueOf(map.get(key)));
            xmlDTO.setDescription(" ");
            xmlDTO.setType("string");
            list.add(xmlDTO);
        }
        return list;
    }

}
