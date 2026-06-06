package com.njht.webyun.zuul.access.filter;

import com.njht.webyun.model.ReqLogEntity;
import com.njht.webyun.model.RespLogEntity;
import com.njht.webyun.utils.HttpUtil;
import com.njht.webyun.utils.IPUtil;
import com.njht.webyun.utils.PathUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 接口调用日志记录过滤器
 */
//@Component
public class LogRecodePostFilter extends ZuulFilter {
 
    private static final Logger logger = LoggerFactory.getLogger(LogRecodePostFilter.class);

    @Value("${log.path.exclude}")
    private String logAuthPath;
 
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }
 
    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 2;
    }
 
    @Override
    public boolean shouldFilter() {
        return true;
    }
 
    @Override
    public Object run() {
        try {
//            logger.info("进入日志记录过滤器");
            RequestContext ctx = RequestContext.getCurrentContext();
            HttpServletRequest request = ctx.getRequest();
            String requestId = UUID.randomUUID().toString();
            long startTime = System.currentTimeMillis();

            String method = request.getMethod();
            String interfaceMethod = request.getServletPath();
            String requestURI = request.getRequestURI();
//            StringBuffer requestURL = request.getRequestURL(); //完整的http://localhost:post/mapping-url
            ReqLogEntity reqEntity = new ReqLogEntity();
            reqEntity.setRequestIp(IPUtil.getIpAddr(request));  // 真实ip地址
            reqEntity.setUri(requestURI);
            reqEntity.setMethod(request.getMethod());
            reqEntity.setSessionId(request.getRequestedSessionId());
            reqEntity.setRequestId(requestId);
            reqEntity.setParamData(HttpUtil.getParamString(request));
            reqEntity.setBodyData(HttpUtil.getBodyString(request));
            reqEntity.setServerName(request.getServerName());
            logger.info("【API请求日志记录】：" + reqEntity);

            for (String path : logAuthPath.split(",",-1)) {
                if (PathUtil.isPathMatch(path,requestURI )) {
                    logger.info("配置为不记录响应报文："+requestURI);
                    return null;
                }
            }


            // 打印response
            InputStream out = ctx.getResponseDataStream();
            String outBody = StreamUtils.copyToString(out, Charset.forName("UTF-8"));
            if (outBody == null || "" .equals( outBody)) {
                logger.error("响应参数:={}" + outBody);
            }

            long endTime = System.currentTimeMillis();
            long executeTime = endTime - startTime;

            RespLogEntity respEntity = new RespLogEntity();
            respEntity.setConsumeTime(executeTime);
            respEntity.setRequestId(requestId);
            respEntity.setSessionId(request.getRequestedSessionId());
            respEntity.setResultData(outBody);


            logger.info("【API响应日志记录】：" + respEntity);

            //必须重新写入流//重要！！！
            ctx.setResponseBody(outBody);

        } catch (IOException e) {
            logger.error("LogRecode IO异常", e);
        }
 
        return null;
    }

}