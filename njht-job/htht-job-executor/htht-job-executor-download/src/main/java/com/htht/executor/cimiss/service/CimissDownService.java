package com.htht.executor.cimiss.service;

import cma.music.client.DataQueryClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.htht.executor.cimiss.bean.DownloadInfo;
import com.htht.executor.cimiss.bean.ResultBean;
import com.htht.executor.cimiss.util.CimissMatchTime;
import com.htht.executor.download.entity.CimissDownInfoEntity;
import com.htht.executor.download.service.CimissDownInfoService;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.entity.paramtemplate.CimissDownParam;
import com.htht.job.core.exception.CommonException;
import com.htht.job.core.util.FormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author daiguojun
 * @date 2022-08-09 10:02
 * 抽象类
 */
@Slf4j
public abstract class CimissDownService implements CimissDownInterface {

    private static final String timeSuffix = "000000";

    @Autowired
    private CimissDownInfoService cimissDownInfoService;

    @Override
    public void down() {
        TriggerParam triggerParam = XxlJobHelper.getTriggerParam();
        CimissDownParam cimissParam = getCimissDataParam(triggerParam.getTaskParam());
        /* 1. 定义client对象 */
        DataQueryClient client = new DataQueryClient();
        /* 2. 调用方法的参数定义，并赋值 */
        XxlJobHelper.log("定义调用方法的参数");
        /* 2.1 用户名&密码 */
        CimissDownInfoEntity info = cimissDownInfoService
                .getOne(new LambdaQueryWrapper<CimissDownInfoEntity>()
                        .eq(CimissDownInfoEntity::getName,"cimiss"));
        String userId = info.getUserName();
        String pwd = info.getPwd();
        /* 2.2  接口ID */
        String interfaceId = cimissParam.getInterfaceId();
        /* 2.3  接口参数，多个参数间无顺序 */
        HashMap<String, String> params = preHttpParam(cimissParam);
        /* 2.4 返回文件的格式 */
        String dataFormat = cimissParam.getDataFormat();
        /* 2.5 返回字符串 */
        StringBuffer retStr = new StringBuffer();
        log.info("root file:"+ new File("").getAbsolutePath());
        /* 3. 调用接口 */
        XxlJobHelper.log("开始调用");
        try {
            //初始化接口服务连接资源
            client.initResources();
            //调用接口
            int rst = client.callAPI_to_serializedStr(userId, pwd, interfaceId, params, dataFormat, retStr);
            if (rst == 0) {
                // 工具类输出，默认输出前2000行
                FormatUtil formatUtil = new FormatUtil();
                formatUtil.outputRstJson( retStr.toString());
                XxlJobHelper.log("数据请求成功");
            } else {
                log.error("[error] StaElemSearchAPI_CLIB_callAPI_to_serializedStr_JSON.");
                log.error("\treturn code: %d. \n", new Object[] { Integer.valueOf(rst) });
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException("[error] StaElemSearchAPI_CLIB_callAPI_to_serializedStr_JSON.");
        } finally {
            //释放接口服务连接资源
            client.destroyResources();
        }
        // 根据不同的实现类执行不同的业务逻辑（下载 或者 入库）
        this.execute(cimissParam,retStr);
    }


    /**
     * 处理请求参数
     * @param downParam
     * @return
     */
    private HashMap<String, String> preHttpParam(CimissDownParam downParam) {
        HashMap<String, String> map = new HashMap<>();
        map.put("dataCode", downParam.getDataCode());
        if (!Objects.isNull(downParam.getAdminCodes()) && StringUtils.isNotBlank(downParam.getAdminCodes())) {
            map.put("adminCodes", downParam.getAdminCodes());
        }
        if (!Objects.isNull(downParam.getStaIds()) && StringUtils.isNotBlank(downParam.getStaIds())) {
            map.put("staIds", downParam.getStaIds());
        }
        String times = downParam.getTime();
        String timeRange = downParam.getTimeRange();
        try {
            if (downParam.getInterfaceId().contains("TimeRange")) {
                downParam.setTimeRange(timeRange);
                timeRange = getCurrentDayRange(timeRange);
                downParam.setTimeRange(timeRange);
                map.put("timeRange", timeRange);
                log.info("timeRange:" + timeRange);
            } else {
                times = getCurrentDay(times, downParam.getDataCode());
                downParam.setTime(times);
                this.setTimes(map,times);
                log.info("times:" + times);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(downParam.getElements())){
            map.put("elements",downParam.getElements());
        }

        if (!Objects.isNull(downParam.getEleValueRanges()) && StringUtils.isNotBlank(downParam.getEleValueRanges())) {
            map.put("eleValueRanges", downParam.getEleValueRanges());
        }
        return map;
    }



    /**
     * 获取CIMISS参数
     * @param taskParam
     * @return
     */
    public CimissDownParam getCimissDataParam(TaskParam taskParam) {
        CimissDownParam cimissDownParam = null;
        try {
            //获取到固定参数
            cimissDownParam = JSON.parseObject(taskParam.getModelParameters(), CimissDownParam.class);
        } catch (Exception e) {
            e.printStackTrace();
            XxlJobHelper.handleFail("CIMISS下载数据参数获取失败");
            throw new CommonException("CIMISS下载数据参数获取失败,"+e.getMessage());
        }
        return cimissDownParam;
    }

    /**
     * 获取时间点
     * @param times
     * @param timeKey
     * @return
     * @throws ParseException
     */
    @Override
    public String getCurrentDay(String times, String timeKey) throws ParseException {
        SimpleDateFormat fromat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        if (StringUtils.isNotBlank(times)) {
            if (times.startsWith("{")) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                times = CimissMatchTime.match(c, times, timeKey);
            } else {
                times = fromat.format(fromat.parse(times));
            }
        } else {
            times = fromat.format(date);
        }
        return times + timeSuffix;
    }


    /**
     * 获取时间范围
     * @param timeRange
     * @return
     * @throws ParseException
     */
    private String getCurrentDayRange(String timeRange) throws ParseException {
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        SimpleDateFormat fromat = new SimpleDateFormat("yyyyMMdd");
        if (StringUtils.isNotBlank(timeRange)) {
            String regex = "\\d{8}-\\d{8}";
            String regex1 = "^[0-9]*$";
            Pattern pattern = Pattern.compile(regex);
            Matcher m = pattern.matcher(timeRange);

            Pattern pattern1 = Pattern.compile(regex1);
            Matcher m1 = pattern1.matcher(timeRange);
            if(m.find()){
                String date1 = timeRange.split("-")[0].substring(0,8);
                String date2 = timeRange.split("-")[1].substring(0,8);
                buf.append(fromat.format(fromat.parse(date1)));
                buf.append(timeSuffix);
                buf.append(",");
                buf.append(fromat.format(fromat.parse(date2)));
                buf.append("235959");
            } else if(m1.find()){
                //默认输入纯数字为天数，当前时间往前
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                Integer i = Integer.valueOf(timeRange);
                calendar.add(Calendar.DATE,-i);
                Date time = calendar.getTime();
                buf.append(fromat.format(time));
                buf.append(timeSuffix);
                buf.append(",");
                buf.append(fromat.format(date));
                buf.append("235959");
            }else{
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, -1);
                Date date = c.getTime();
                buf.append(fromat.format(date));
                buf.append(timeSuffix);
                buf.append(",");
                buf.append(fromat.format(date));
                buf.append("235959");
            }
        } else {
            Calendar c = Calendar.getInstance();
            c.add(5, -1);
            Date date = c.getTime();
            buf.append(fromat.format(date));
            buf.append(timeSuffix);
            buf.append(",");
            buf.append(fromat.format(date));
            buf.append("235959");
        }
        buf.append("]");
        return buf.toString();
    }

    @Override
    public ResultBean getFileOrStationData(String rstData) {
        ResultBean resultBean = new ResultBean();
        JSONObject jsonObject = JSONObject.parseObject(rstData);
        String returnCode = (String)jsonObject.get("returnCode");
        resultBean.setReturnCode(returnCode);
        resultBean.setReturnMessage((String)jsonObject.get("returnMessage"));
        String fileCount = (String)jsonObject.get("fileCount");
        String rowCount = (String)jsonObject.get("rowCount");
        if (StringUtils.isNotBlank(fileCount)) {
            resultBean.setFileResult(true);
            log.info("--CimissHandler--fileCount--下载文件数量：{}",fileCount);
            resultBean.setRowCount(fileCount);
        } else if (StringUtils.isNotBlank(rowCount)) {
            resultBean.setFileResult(false);
            log.info("--CimissHandler--rowCount--站点数据数量：{}",rowCount);
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
    private  void setFileResult(ResultBean resultBean, JSONArray array) {
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

    private void setGridResult(ResultBean resultBean, JSONArray array) {
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
            log.error(element +"为null----------------");
        }
        resultBean.setData(dataList);
    }

}
