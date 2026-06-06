package com.htht.executor.cimiss.util;

import cma.music.utils.HttpDownLoad;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htht.executor.cimiss.bean.DownloadInfo;
import com.htht.executor.cimiss.bean.ResultBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author daiguojun
 * @date 2022-08-11 10:44
 * 数据下载
 */
public class HttpsUtil {

    public static boolean downLoadFromHttpsUrl(String httpsUrl,String fileName,String filePath) {

        boolean flag = true;

        final RestTemplate restTemplate = new RestTemplate();
        /* 请求头 */
        final HttpHeaders httpHeaders = new HttpHeaders();
        // 请求实体
        final HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(httpsUrl, HttpMethod.GET, httpEntity, byte[].class);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            byte[] bytes = responseEntity.getBody();

            try (FileOutputStream out = new FileOutputStream(new File(filePath,fileName))) {
                out.write(bytes, 0, bytes.length);
                out.flush();
            } catch (Exception e) {
                flag = false;
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static void main(String[] args) {

        String url = "http://10.181.89.55/cimiss-web/api?userId=BEXN_QHZX_kindhorse&pwd=8203130&interfaceId=getNafpFileByTimeRange&dataCode=NAFP_CLDAS2.0_RT_5km_DAY_GRB&timeRange=[20220815000000,20220816010000]&dataFormat=json";
        RestTemplate restTemplate = new RestTemplate();
        String resultData = restTemplate.getForObject(url, String.class);

        ResultBean resultBean = getFileOrStationData(resultData);
        List<DownloadInfo> data = resultBean.getData();

        AtomicReference<String> atomicReference = new AtomicReference<>("Y:\\qh\\GRB2");

        String regex = "\\d{14}_.*.(\\d{10}).GRB2";
        Pattern pattern = Pattern.compile(regex);

        for (DownloadInfo downloadInfo :data) {
            String fileName = downloadInfo.getFileName();
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                String group = matcher.group();
                String filePath = atomicReference.get() +File.separator+ group.substring(0,8);

                try {
                    FileUtils.forceMkdir(new File(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HttpDownLoad.httpDownload(downloadInfo.getFileURL(),new File(filePath,fileName).getPath());
            }
        }

    }

    public static ResultBean getFileOrStationData(String rstData) {
        ResultBean resultBean = new ResultBean();
        JSONObject jsonObject = JSONObject.parseObject(rstData);
        String returnCode = (String)jsonObject.get("returnCode");
        resultBean.setReturnCode(returnCode);
        resultBean.setReturnMessage((String)jsonObject.get("returnMessage"));
        String fileCount = (String)jsonObject.get("fileCount");
        String rowCount = (String)jsonObject.get("rowCount");
        if (StringUtils.isNotBlank(fileCount)) {
            resultBean.setFileResult(true);
//            log.info("--CimissHandler--fileCount--下载文件数量：{}",fileCount);
            resultBean.setRowCount(fileCount);
        } else if (StringUtils.isNotBlank(rowCount)) {
            resultBean.setFileResult(false);
//            log.info("--CimissHandler--rowCount--站点数据数量：{}",rowCount);
            resultBean.setRowCount(rowCount);
        } else {
            resultBean.setReturnCode("-1");
            resultBean.setReturnMessage("fileCount && rowCount = null");
            resultBean.setRowCount("0");
            resultBean.setColCount("0");
            return resultBean;
        }
        resultBean.setColCount((String)jsonObject.get("colCount"));
        resultBean.setRequestParams((String)jsonObject.get("requestParams"));
        resultBean.setRequestTime((String)jsonObject.get("requestTime"));
        resultBean.setResponseTime((String)jsonObject.get("responseTime"));
        resultBean.setTakeTime((String)jsonObject.get("takeTime"));
        String fieldNames = (String)jsonObject.get("fieldNames");
        if (null != fieldNames) {
            String[] fieldNamesArr = fieldNames.split(" ");
            resultBean.setFieldNames(fieldNamesArr);
        }
        String fieldUnits = (String)jsonObject.get("fieldUnits");
        if (null != fieldUnits) {
            String[] fieldUnitsArr = fieldUnits.split(" ");
            resultBean.setFieldUnits(fieldUnitsArr);
        }
        if ("0".equals(returnCode)) {
            JSONArray array = jsonObject.getJSONArray("DS");
            if (resultBean.isFileResult()) {
                setFileResult(resultBean, array);
            } else {
                setGridResult(resultBean, array);
            }
        }
        return resultBean;
    }

    public static void setFileResult(ResultBean resultBean, JSONArray array) {
        List<DownloadInfo> data = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            DownloadInfo info = new DownloadInfo();
            info.setFileName(obj.getString("FILE_NAME"));
            info.setFileSize(obj.getString("FILE_SIZE"));
            info.setFormat(obj.getString("FORMAT"));
            info.setFileURL(obj.getString("FILE_URL"));
            data.add(info);
        }
        resultBean.setData(data);
    }

    public static void setGridResult(ResultBean resultBean, JSONArray array) {
        List<Map<String, String>> dataList = new ArrayList<>();
        String requestParams = resultBean.getRequestParams();
        String[] params = requestParams.split("&");
        String element = null;
        for (String param : params) {
            if (param.indexOf("elements") > -1) {
                element = param;
                break;
            }
        }
        if (element != null) {
            String elementValue = element.substring(element.indexOf("=") + 1);
            String[] elements = elementValue.split(",");
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Map<String, String> dataMap = new HashMap<>();
                for (String field : elements)
                    dataMap.put(field, obj.getString(field));
                dataList.add(dataMap);
            }
        } else {
//            log.error(element +"为null----------------");
        }
        resultBean.setData(dataList);
    }
}
