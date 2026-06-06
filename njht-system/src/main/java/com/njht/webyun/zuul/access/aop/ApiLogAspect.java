package com.njht.webyun.zuul.access.aop;

import com.alibaba.fastjson.JSON;
import com.njht.webyun.model.ReqLogEntity;
import com.njht.webyun.model.RespLogEntity;
import com.njht.webyun.utils.HttpUtil;
import com.njht.webyun.utils.IPUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;    
import org.aspectj.lang.annotation.*;    
import org.slf4j.Logger;    
import org.slf4j.LoggerFactory;    
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**    
 * @author www.exception.site (exception 教程网)    
 * @date 2019/2/12    
 * @time 14:03    
 * @discription    
 **/    
@Aspect   //告诉Spring这是个切面类
@Component    //转换成Spring容器中的bean
public class ApiLogAspect {

    private final static Logger logger = LoggerFactory.getLogger(ApiLogAspect.class);
    private final String REQUEST_ID = "requestId";
    private final String START_TIME = "startTime";

    /**
     * 以 controller 包下定义的所有请求为切入点  只有public方法才拦截
     * apiLog()  签名，可以理解成这个切入点的一个名称
     * */
    @Pointcut("execution(public * com.njht.webyun.*.controller..*.*(..))")
    public void systemLog() {}
//    @Pointcut("execution(* com.aoto.zuul.client..*.*(..))")
    @Pointcut("execution(* com.njht.webyun.zuul.client.TestClient.getUserByName(..))")
    public void clientLog() {}
    /**    
     * 在切点之前织入    controller 和 @FeignClient 之前要干
     * @param joinPoint    
     * @throws Throwable    
     */    
    @Before("systemLog()||clientLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {    
        // 开始打印请求日志
        //这个RequestContextHolder是Springmvc提供来获得请求的东西
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();    
        HttpServletRequest request = attributes.getRequest();    
        // 打印请求相关参数    
        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();
        request.setAttribute(START_TIME, startTime);
        request.setAttribute(REQUEST_ID, requestId);

        ReqLogEntity reqEntity = new ReqLogEntity();
        reqEntity.setRequestIp(IPUtil.getIpAddr(request));  // 真实ip地址
        reqEntity.setUri(request.getRequestURI());
        reqEntity.setMethod(request.getMethod());
        reqEntity.setSessionId(request.getRequestedSessionId());
        reqEntity.setRequestId(requestId);
        reqEntity.setParamData(HttpUtil.getParamString(request));
        Object object[] = joinPoint.getArgs();
        Object body;
        if(object.length>1){
            body = object[0];
        }else{
            if(object.length == 1  && (object[0] instanceof HttpServletResponse||object[0] instanceof MultipartFile)){
                logger.info("【API请求日志记录】无参："+request.getRequestURI());
                return;
            }
            body = object;
        }
        reqEntity.setBodyData(JSON.toJSONString(body));
        reqEntity.setServerName(request.getServerName());

        logger.info("【API请求日志记录】："+ reqEntity);
    }
    /**    
     * 在切点之后织入    
     * @throws Throwable    
     */    
    @After("systemLog()||clientLog()")
    public void doAfter() throws Throwable {    
//        logger.info("=========================================== End ===========================================");

    }

    /**    
     * 环绕    
     * @param proceedingJoinPoint    
     * @return    
     * @throws Throwable    
     */    
    @Around("systemLog()||clientLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed(); // 返回值
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        long startTime = (Long) request.getAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;

        RespLogEntity respEntity = new RespLogEntity();
        respEntity.setConsumeTime(executeTime);
        respEntity.setRequestId((String) request.getAttribute(REQUEST_ID));
        respEntity.setSessionId(request.getRequestedSessionId());
        respEntity.setResultData(JSON.toJSONString(result));


        logger.info("【API响应日志记录】："+respEntity);
        return result;
    }
}