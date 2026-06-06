package com.htht.job.admin.dispatch.core.trigger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.htht.job.admin.dispatch.core.conf.XxlJobAdminConfig;
import com.htht.job.admin.dispatch.core.exception.XxlJobException;
import com.htht.job.admin.dispatch.core.model.XxlJobGroup;
import com.htht.job.admin.dispatch.core.model.XxlJobInfo;
import com.htht.job.admin.dispatch.core.model.XxlJobLog;
import com.htht.job.admin.dispatch.core.route.ExecutorRouteStrategyEnum;
import com.htht.job.admin.dispatch.core.scheduler.XxlJobScheduler;
import com.htht.job.admin.dispatch.core.util.I18nUtil;
import com.htht.job.admin.template.vo.TemplateParamReqVo;
import com.htht.job.core.biz.ExecutorBiz;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TaskParametersEntity;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.enums.ExecutorBlockStrategyEnum;
import com.htht.job.core.enums.RegistryConfig;
import com.htht.job.core.glue.GlueTypeEnum;
import com.htht.job.core.util.IpUtil;
import com.htht.job.core.util.ThrowableUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * pie-job trigger
 * Created by piesat on 17/7/13.
 */
public class XxlJobTrigger {
    private static Logger logger = LoggerFactory.getLogger(XxlJobTrigger.class);

    public static ObjectMapper objectMapper = XxlJobAdminConfig.getAdminConfig().getObjectMapper();

    private XxlJobTrigger() {}

    /**
     * trigger job
     *
     * @param jobId
     * @param triggerType
     * @param failRetryCount
     * 			>=0: use this param
     * 			<0: use param from job info config
     * @param executorShardingParam
     * @param executorParam
     *          null: use job param
     *          not null: cover job param
     * @param addressList
     *          null: use executor addressList
     *          not null: cover
     */
    public static void trigger(int jobId, TriggerTypeEnum triggerType, int failRetryCount, String executorShardingParam, String executorParam, String addressList) {
        // load data
        XxlJobInfo jobInfo = XxlJobAdminConfig.getAdminConfig().getXxlJobInfoDao().loadById(jobId);
        // 根据任务id 获取对应 任务参数
        TaskParametersEntity taskParametersEntity = XxlJobAdminConfig.getAdminConfig().getTaskParametersDao().loadByJobId(jobId);
        if (jobInfo == null) {
            logger.warn(">>>>>>>>>>>> trigger fail, jobId invalid，jobId={}", jobId);
            return;
        }
        if (executorParam != null) {
            jobInfo.setExecutorParam(executorParam);
        }
        int finalFailRetryCount = failRetryCount>=0?failRetryCount:jobInfo.getExecutorFailRetryCount();
        XxlJobGroup group = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().load(jobInfo.getJobGroup());

        // cover addressList 页面输入地址用页面地址,没有输入用算法绑定的 节点,没有绑定算法用该执行器对应的任意节点
        if ((addressList == null || addressList.trim().length() == 0) && !StringUtils.isEmpty(jobInfo.getAlgorithmId())) {
            // 执行节点从绑定的算法上面去获取
            addressList = XxlJobAdminConfig.getAdminConfig().getAlgorithmDao().selectAddressListById(jobInfo.getAlgorithmId());
        }
        // addressList 可能是前端传参输入,也可能是算法绑定
        if (addressList!=null && addressList.trim().length()>0) {
            group.setAddressType(1);
            group.setAddressList(addressList.trim());
        }
        // sharding param
        int[] shardingParam = null;
        if (executorShardingParam!=null){
            String[] shardingArr = executorShardingParam.split("/");
            if (shardingArr.length==2 && isNumeric(shardingArr[0]) && isNumeric(shardingArr[1])) {
                shardingParam = new int[2];
                shardingParam[0] = Integer.parseInt(shardingArr[0]);
                shardingParam[1] = Integer.parseInt(shardingArr[1]);
            }
        }

        // 初始化TriggerParam
        TriggerParam triggerParam = XxlJobTrigger.initTriggerParam(jobInfo,shardingParam,taskParametersEntity);
        // 此处根据 jobInfo 获取任务调度方式，区分python和java,python不做分片
        GlueTypeEnum glueTypeEnum = GlueTypeEnum.match(jobInfo.getGlueType());
        if (GlueTypeEnum.BEAN==glueTypeEnum) {
            // java代码 做分片处理，其余不进行分片
            XxlJobTrigger.executeBean(jobInfo,group,triggerParam,finalFailRetryCount,triggerType,addressList);
        } else {
            processTrigger(group, jobInfo, finalFailRetryCount, triggerType, triggerParam);
        }
    }

    /**
     * java 分片处理，shard
     * @param jobInfo
     * @param group
     * @param triggerParam
     * @param finalFailRetryCount
     * @param triggerType
     * @param addressList
     */
    private static void executeBean(XxlJobInfo jobInfo,XxlJobGroup group,TriggerParam triggerParam,int finalFailRetryCount,TriggerTypeEnum triggerType,String addressList) {
        List<String> executeList = new ArrayList<>();
        String msg = null;
        // 分片 依据调度策略进行分片
        ExecutorRouteStrategyEnum executorRouteStrategyEnum = ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null);
        // 获取分片地址,地址为空时 在runShardExecutor中抛出异常
        String address = XxlJobTrigger.initAddress(group,executorRouteStrategyEnum,triggerParam);
        // 分片 开始调度时间
        Date triggerDate = new Date();
        try {
            ReturnT<List<String>> shardReturn = XxlJobTrigger.runShardExecutor(triggerParam,address);
            msg = shardReturn.getMsg();
            if (shardReturn.getCode() != ReturnT.SUCCESS_CODE ) {
                logger.info(">>>>>>>>>>>> Shard error,jobId={},{}",jobInfo.getId(),msg);
                XxlJobTrigger.updateXxlJobShardLogInfo(msg,jobInfo,addressList,finalFailRetryCount,triggerDate,ReturnT.FAIL_CODE);
                return;
            }
            if (shardReturn.getData() != null) {
                executeList = shardReturn.getData();
            }
        } catch (Exception e) {
            // shard 处理异常返回调度异常信息
            logger.info(">>>>>>>>>>>> Shard error,jobId={},{}",jobInfo.getId(),e.getMessage());
            XxlJobTrigger.updateXxlJobShardLogInfo(e.getMessage(),jobInfo,addressList,finalFailRetryCount,triggerDate,ReturnT.FAIL_CODE);
            return;
        }
        // 没有异常,调度成功，没有要处理的数据
        if ( executeList == null || executeList.isEmpty()){
            // shard 返回结果为空,没有要处理的文件信息，执行结束,没有数据默认执行成功。
            XxlJobTrigger.updateXxlJobShardLogInfo(msg,jobInfo,addressList,finalFailRetryCount,triggerDate,ReturnT.SUCCESS_CODE_NODATA);
            logger.info(">>>>>>>>>>>> Shard success,没有要处理的数据 jobId invalid，jobId={}", jobInfo.getId());
            return;
        }

        for (String param:executeList) {
            // param 分发到不同的节点执行
            logger.info("exec task......");
            jobInfo.setExecutorParam(param);
            processTrigger(group, jobInfo, finalFailRetryCount, triggerType, triggerParam);
        }
    }

    /**
     * shard 没有要处理的数据，生成日志 并返回
     * @param msg
     * @param triggerDate
     */
    private static void updateXxlJobShardLogInfo(String msg, XxlJobInfo jobInfo, String addressList, Integer finalFailRetryCount, Date triggerDate,int code) {
        if (msg == null) {
            msg = I18nUtil.getString("shard_fail_msg");
        }
        XxlJobLog xxlJobLog = XxlJobTrigger.saveXxlJobLogBeforeExecute(jobInfo,triggerDate);
        XxlJobTrigger.updateXxlJobLogAfterExecute(xxlJobLog,jobInfo,addressList,null,finalFailRetryCount,ReturnT.SUCCESS,I18nUtil.getString("joblog_status_suc"));
        // 生成执行日志 并入库
        xxlJobLog.setHandleTime(new Date());
        xxlJobLog.setHandleCode(code);
        xxlJobLog.setHandleMsg(msg);
        XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().updateHandleInfo(xxlJobLog);
    }

    private static boolean isNumeric(String str){
        try {
            Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @param group   job group, registry list may be empty
     * @param jobInfo
     * @param finalFailRetryCount
     * @param triggerType
     * @param triggerParam
     */
    private static void processTrigger(XxlJobGroup group, XxlJobInfo jobInfo, int finalFailRetryCount, TriggerTypeEnum triggerType,TriggerParam triggerParam){
//        int index = triggerParam.getBroadcastIndex();
//        int total = triggerParam.getBroadcastTotal();
        // param
        // block strategy
        ExecutorBlockStrategyEnum blockStrategy = ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), ExecutorBlockStrategyEnum.SERIAL_EXECUTION);
        // route strategy
        ExecutorRouteStrategyEnum executorRouteStrategyEnum = ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null);
//        String shardingParam = (ExecutorRouteStrategyEnum.SHARDING_BROADCAST==executorRouteStrategyEnum)?String.valueOf(index).concat("/").concat(String.valueOf(total)):null;
        String shardingParam = null;

        // 1、save log-id
        XxlJobLog jobLog = XxlJobTrigger.saveXxlJobLogBeforeExecute(jobInfo,new Date());
        logger.debug(">>>>>>>>>>> pie-job trigger start, jobId:{}", jobLog.getId());

        // 2、 trigger-param中添加日志相关信息
        triggerParam.setExecutorParams(jobInfo.getExecutorParam());
        triggerParam.setLogId(jobLog.getId());
        triggerParam.setLogDateTime(jobLog.getTriggerTime().getTime());

        // 3、init address
        ReturnT<String> routeAddressResult = new ReturnT<>();
        String address = XxlJobTrigger.initAddress(group, executorRouteStrategyEnum,triggerParam);


        // 4、trigger remote executor
        ReturnT<String> triggerResult = null;
        if (address != null) {
            triggerResult = runExecutor(triggerParam, address);
        } else {
            routeAddressResult = new ReturnT<>(ReturnT.FAIL_CODE, I18nUtil.getString("jobconf_trigger_address_empty"));
            triggerResult = new ReturnT<>(ReturnT.FAIL_CODE, null);
        }

        // 5、collection trigger info
        StringBuffer triggerMsgSb = new StringBuffer();
        triggerMsgSb.append(I18nUtil.getString("jobconf_trigger_type")).append("：").append(triggerType.getTitle());
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobconf_trigger_admin_adress")).append("：").append(IpUtil.getIp());
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobconf_trigger_exe_regtype")).append("：")
                .append( (group.getAddressType() == 0)?I18nUtil.getString("jobgroup_field_addressType_0"):I18nUtil.getString("jobgroup_field_addressType_1") );
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobconf_trigger_exe_regaddress")).append("：").append(group.getRegistryList());
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorRouteStrategy")).append("：").append(executorRouteStrategyEnum.getTitle());
        if (shardingParam != null) {
            triggerMsgSb.append("("+shardingParam+")");
        }
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorBlockStrategy")).append("：").append(blockStrategy.getTitle());
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_timeout")).append("：").append(jobInfo.getExecutorTimeout());
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorFailRetryCount")).append("：").append(finalFailRetryCount);

        triggerMsgSb.append("<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>"+ I18nUtil.getString("jobconf_trigger_run") +"<<<<<<<<<<< </span><br>")
                .append((routeAddressResult.getMsg()!=null)?routeAddressResult.getMsg()+"<br><br>":"").append(triggerResult.getMsg()!=null?triggerResult.getMsg():"");

        // 6、save log trigger-info
        logger.info(">>>>>>>>>>pie-job trigger update db");
        XxlJobTrigger.updateXxlJobLogAfterExecute(jobLog,jobInfo,address,shardingParam,finalFailRetryCount,triggerResult,triggerMsgSb.toString());


        logger.info(">>>>>>>>>>> pie-job trigger end, jobId:{}", jobLog.getId());
    }

    /**
     * 任务调度结束后，保存相关日志信息
     * @param jobLog
     * @param jobInfo
     * @param triggerResult
     * @param triggerMsgSb
     */
    private static void updateXxlJobLogAfterExecute(XxlJobLog jobLog, XxlJobInfo jobInfo, String address, String shardingParam, Integer finalFailRetryCount, ReturnT<String> triggerResult, String triggerMsgSb) {
        jobLog.setExecutorAddress(address);
        jobLog.setExecutorHandler(jobInfo.getExecutorHandler());
        jobLog.setExecutorParam(jobInfo.getExecutorParam());
        jobLog.setExecutorShardingParam(shardingParam);
        jobLog.setExecutorFailRetryCount(finalFailRetryCount);
        jobLog.setTriggerCode(triggerResult.getCode());
        jobLog.setTriggerMsg(triggerMsgSb);
        logger.info("update trigger info,code:{},msg{}",triggerResult.getCode(),triggerMsgSb);
        XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().updateTriggerInfo(jobLog);
    }

    /**
     * handler 调度之前执行日志
     * @param jobInfo
     */
    private static XxlJobLog saveXxlJobLogBeforeExecute(XxlJobInfo jobInfo,Date date) {
        XxlJobLog jobLog = new XxlJobLog();
        jobLog.setJobGroup(jobInfo.getJobGroup());
        jobLog.setJobId(jobInfo.getId());
        jobLog.setTriggerTime(date);
        jobLog.setTreeId(jobInfo.getTreeId());
        XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().save(jobLog);
        return jobLog;
    }

    /**
     * 初始化执行节点
     * @param group
     * @param executorRouteStrategyEnum
     * @param triggerParam
     * @return
     */
    private static String initAddress(XxlJobGroup group, ExecutorRouteStrategyEnum executorRouteStrategyEnum,TriggerParam triggerParam) {
        String address = null;
        if (group.getRegistryList()!=null && !group.getRegistryList().isEmpty()) {
            // 自动注册 和 手动注册 两种方式先检查节点是否可用,并过滤掉不可用节点
            List<String> registryList = filterRegistryList(group.getRegistryList(),group.getAppname());
            if (!registryList.isEmpty()) {
                // 根据执行策略 找到要执行的节点
                ReturnT<String> routeAddressResult = executorRouteStrategyEnum.getRouter().route(triggerParam, registryList);
                if (routeAddressResult.getCode() == ReturnT.SUCCESS_CODE) {
                    address = routeAddressResult.getData();
                }
            }
        }
        return address;
    }

    /**
     * 判断当前节点是否可用，过滤掉不可用节点
     * @param registryList
     * @param appName
     */
    private static List<String> filterRegistryList(List<String> registryList, String appName) {
        // 查询数据库中当前执行器可用节点
        List<String> dbRegistryList =
                XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryDao().findRegistryValueList(RegistryConfig.DEAD_TIMEOUT, new Date(),appName);
        // 判断 registryList 是否有可用节点,有返回可用节点,没有返回空集合
        return Optional.ofNullable(dbRegistryList).orElse(new ArrayList<>())
                .stream()
                .filter(registryList::contains)
                .collect(Collectors.toList());
    }

    /**
     * 初始化 triggerParam
     * @param jobInfo
     * @param shardingParam
     * @param taskParametersEntity 页面动态参数
     * @return
     */
    private static TriggerParam initTriggerParam(XxlJobInfo jobInfo, int[] shardingParam, TaskParametersEntity taskParametersEntity) {
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(jobInfo.getId());
        triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
        triggerParam.setExecutorParams(jobInfo.getExecutorParam());
        triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
        triggerParam.setExecutorTimeout(jobInfo.getExecutorTimeout());
        triggerParam.setGlueType(jobInfo.getGlueType());
        triggerParam.setGlueSource(jobInfo.getGlueSource());
        triggerParam.setGlueUpdatetime(jobInfo.getGlueUpdatetime().getTime());

        if (shardingParam == null) {
            shardingParam = new int[]{0, 1};
        }
        triggerParam.setBroadcastIndex(shardingParam[0]);
        triggerParam.setBroadcastTotal(shardingParam[1]);

        // 初始化任务参数
        TaskParam taskParam = new TaskParam();
        BeanUtils.copyProperties(taskParametersEntity,taskParam);
        LinkedHashMap<String,Object> dyMap = XxlJobTrigger.getParamMap(taskParametersEntity.getDynamicParameter());
        taskParam.setDynamicMap(dyMap);
        LinkedHashMap<String,Object> fixMap = XxlJobTrigger.getParamMap(taskParametersEntity.getFixedParameter());
        taskParam.setFixedMap(fixMap);
        triggerParam.setTaskParam(taskParam);
        // 调度参数
        String modelParam = XxlJobTrigger.getParamJsonStr(taskParametersEntity.getModelParameters());
        taskParam.setModelParameters(modelParam);
        return triggerParam;
    }

    /**
     * 处理调度参数,只获取key value 并返回json字符串
     * @param modelParameters
     * @return
     */
    public static String getParamJsonStr(String modelParameters) {
        String s = null;
        LinkedHashMap<String, Object> paramMap = XxlJobTrigger.getParamMap(modelParameters);
        try {
            s = objectMapper.writeValueAsString(paramMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 获取动态参数。固定参数map
     * @param jsonStr
     * @return
     */
    private static LinkedHashMap<String, Object> getParamMap(String jsonStr) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        //前端传参 动态参数为map集合
        if (StringUtils.isBlank(jsonStr)) {
            return map;
        }
        List<TemplateParamReqVo> templateParamReqVoList = new ArrayList<>();
        try {
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, TemplateParamReqVo.class);
            templateParamReqVoList = objectMapper.readValue(jsonStr,listType);
        } catch (Exception e) {
            logger.error("------->>>>>页面传参格式转换异常-----,{}",e.getMessage());
            // 抛异常返回空map
            return map;
        }
        // 将list 集合中的参数放到 map中
        for (TemplateParamReqVo c:templateParamReqVoList) {
            map.put(c.getIdentify(), c.getCurrentValue());
        }
        return map;
    }

    /**
     * run executor
     * @param triggerParam
     * @param address
     * @return
     */
    public static ReturnT<String> runExecutor(TriggerParam triggerParam, String address){
        ReturnT<String> runResult = null;
        try {
            ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(address);
            runResult = executorBiz.run(triggerParam);
        } catch (Exception e) {
            logger.error(">>>>>>>>>>> pie-job trigger error, please check if the executor[{}] is running.", address, e);
            runResult = new ReturnT<>(ReturnT.FAIL_CODE, ThrowableUtil.toString(e));
        }

        StringBuilder runResultSB = XxlJobTrigger.setReturnMsg(address,runResult);
        runResult.setMsg(runResultSB.toString());
        return runResult;
    }

    /**
     * run shard executor
     * @param triggerParam
     * @param address
     * @return
     */
    private static ReturnT<List<String>> runShardExecutor(TriggerParam triggerParam,String address) throws Exception{
        ReturnT<List<String>> runShardResult;
        if (address == null) {
            logger.error(">>>>>>>>>>> pie-job shard trigger error, please check if the executor is exist.");
            throw new XxlJobException("执行器节点 或 算法绑定执行器节点 未正常运行");
        }
        ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(address);
        if (executorBiz == null) {
            //根据地址获取执行节点出错
            logger.error(">>>>>>>>>>> pie-job shard trigger error, please check if the executor [{}] is running.",address);
            throw new XxlJobException("执行器节点不可用,请检查节点是否正常运行,<br> address:"+address);
        }
        runShardResult = executorBiz.runShard(triggerParam);
        if (runShardResult == null){
            logger.error(">>>>>>>>>>> pie-job shard trigger error, please check if the shard is return. address:[{}]", address);
            throw new XxlJobException(triggerParam.getExecutorHandler()+"shard 没有设置返回结果");
        }
        StringBuilder runResultSb = XxlJobTrigger.setReturnMsg(address,runShardResult);
        runShardResult.setMsg(runResultSb.toString());
        return runShardResult;
    }

    /**
     * 添加 调度返回信息
     * @param address
     * @param runResult
     * @return
     */
    private static StringBuilder setReturnMsg(String address, ReturnT<?> runResult) {
        StringBuilder runResultSB = new StringBuilder(I18nUtil.getString("jobconf_trigger_run") + "：");
        runResultSB.append("<br>address：").append(address);
        runResultSB.append("<br>code：").append(runResult.getCode());
        runResultSB.append("<br>msg：").append(runResult.getMsg());

        return runResultSB;
    }

}
