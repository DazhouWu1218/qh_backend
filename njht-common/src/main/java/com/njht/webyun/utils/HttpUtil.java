package com.njht.webyun.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author tianjm
 * @date 2021/2/1
 * @description：
 */
public class HttpUtil {

    /**
     * 获取请求Body
     *
     * @param request
     * @return
     */
    public static String getBodyString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取get请求param
     *
     * @param request
     * @return
     */
    public static String getParamString(HttpServletRequest request){
        Map parameterMap = request.getParameterMap();
        return MapUtil.getParamString(parameterMap);
    }

    public static Logger loggerUtil= LoggerFactory.getLogger(HttpUtil.class);
    //private static Logger logger = Logger.getLogger(HttpUtil.class);
    private static final String ENCONFIG = "UTF-8";
    private static final RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000)
            .setConnectionRequestTimeout(10000)
            .setSocketTimeout(60000)
            .build();

    public static String get(String url, Map<String, Object> params) {
        return get(url, params, ENCONFIG);
    }

    public static JSONObject getJSONObject(String url, Map<String, Object> params) {
        String result = get(url, params, ENCONFIG);
        if (result==null||result.equals("")) {
            return new JSONObject();
        }
        return JSON.parseObject(result);
    }

    public static JSONArray getJSONArray(String url, Map<String, Object> params) {
        String result = get(url, params, ENCONFIG);
        if (result==null||result.equals("")) {
            return new JSONArray();
        }
        return JSON.parseArray(result);
    }


    /**
     * 带参Get请求
     * @param url
     * @param params
     * @param encoding
     * @return
     */
    public static String get(String url, Map<String, Object> params, String encoding) {

        loggerUtil.info("请求开始："+url);
        Long startTime = Long.valueOf(System.currentTimeMillis());
        if (url==null||url.equals("")) {
            return "";
        }
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = null;
        try {
            if ((params != null) && (!params.isEmpty())) {
                List pairs = new ArrayList(params.size());
                for (String key : params.keySet()) {
                    if (null != params.get(key)) ;
                    {
                        pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
                    }
                }
                url = url + "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, encoding));
            }

            httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response = client.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
                throw new RuntimeException("HttpClient ,error status code :" + statusCode+response.toString());
            }

            HttpEntity entity = response.getEntity();
//            System.out.println("流的长度是："+entity.getContent().available());

//            InputStream in=entity.getContent();
//            if(in.available()>1000){
//              FileOutputStream fos=new FileOutputStream("D:\\test.snb");
//              byte[] b=new byte[in.available()];
//              in.read(b);
//              fos.write(b);
//
//              in.close();
//              fos.close();
//            }
//            byte[] b=new byte[in.available()];
//            in.read(b);
//            String s=new String(b);
//            System.out.println(s);
            //

            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, encoding);
            }

            EntityUtils.consume(entity);
            response.close();

            Long endTime = Long.valueOf(System.currentTimeMillis());
            Long useTime = Long.valueOf(endTime.longValue() - startTime.longValue());

            //System.out.println("HTTP返回：" + result);
            System.out.println("耗时：" + useTime);

            return result;
        } catch (Exception ie) {
            loggerUtil.info("请求异常结束："+ie.getMessage());
            ie.printStackTrace();
            throw new RuntimeException(ie.getMessage());
        } finally {
            if (client != null) {
                try {
                    if (httpGet != null) {
                        httpGet.releaseConnection();
                    }
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    loggerUtil.info("请求异常结束：");
                }
            }

            loggerUtil.info("请求结束："+url);
        }
    }

    /**
     * 带参Post请求
     * @param url
     * @param param
     * @return
     */
    public static String sendPost(String url, JSONObject param){
        System.out.println(url+" : "+param);
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        String result = null;
        HttpURLConnection httpURLConnection = null;

        try{
            URL realUrl = new URL(url);

            httpURLConnection = (HttpURLConnection)realUrl.openConnection();
            httpURLConnection.setRequestProperty("accept","*/*");
            httpURLConnection.setRequestProperty("connection","Keep-Alive");
            httpURLConnection.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
            httpURLConnection.setRequestProperty("Content-Type","application/json;charset=utf-8");
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(),ENCONFIG);

            if(param != null){
                outputStreamWriter.write(param.toJSONString());
            }else{
                outputStreamWriter.write("");
            }

            outputStreamWriter.flush();

            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while((line = bufferedReader.readLine())!=null){
                result = result + line;
            }
        }catch(Exception e){
            System.out.println("发送POST请求出现异常："+e.getMessage());
            e.printStackTrace();
        }finally {
            try{
                if (httpURLConnection !=null){
                    httpURLConnection.disconnect();
                }
                if(outputStreamWriter != null){
                    outputStreamWriter.close();
                }
                if (bufferedReader != null){
                    bufferedReader.close();
                }
            }catch (IOException ie){
                ie.getMessage();
            }
        }

        System.out.println("post推送结果："+result);
        return result;
    }

}
