package com.njht.webyun.publish.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.njht.webyun.publish.common.exception.AllExceptionAdvice;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @description: AOP日志提示
 * @author: hkp
 * @create: 2019-06-10
 * @version: 1.0
 **/
@Slf4j
@Aspect
@Component
public class LogRecordAspect {

	@Value("${spring.profiles.active}")
	private String active;

	private static final String ACTIVE_ZS = "linux";

	@Autowired
	private ObjectMapper mapper;

	/**
	 * 定义切入点，com.njht.webyun.publish路径下且有RestController的进行拦截
	 */
	@Pointcut("@within(org.springframework.web.bind.annotation.RestController)||@annotation(org.springframework.web.bind.annotation.RestController)")
	public void controllerMethodPointcut() {
	}

	/**
	 * 环绕通知
	 * @param joinPoint 连接点
	 * @return 切入点返回值
	 * @throws Throwable 异常信息
	 */
	@Around("controllerMethodPointcut()")
	@SneakyThrows
	public Object apiLog(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		boolean logFlag = this.needToLog(method);
		log.debug("当前配置：[{}]",active);
		if ((!logFlag)&&active.equals(ACTIVE_ZS)) {
			return joinPoint.proceed();
		}

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		// 记录下请求内容
		log.debug("┌──────────请求──────────────────");
		log.debug("┃控制器[{}]->方法[{}]-->参数{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), mapper.writeValueAsString(Arrays.toString(joinPoint.getArgs())));
		log.debug("┃{}-->{}-->{}", request.getRemoteAddr(), request.getMethod(), request.getRequestURL());
		/* 格式化输出的json mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);*/
		log.debug("┃parameter{}", mapper.writeValueAsString(request.getParameterMap()));
		log.debug("┃body{}",getPayload(request));
		log.debug("└──────────────────────────────");
		long startTime = System.currentTimeMillis();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		response.setHeader("Content-Type", "application/json;charset=utf-8");
		Object ob = joinPoint.proceed();
		log.debug("┌──────────回复──────────────────");
		log.debug("┃耗时{}ms" ,(System.currentTimeMillis() - startTime));
		log.debug("└──────────────────────────────");
		return ob;
	}

	private String getPayload(HttpServletRequest request) {
		ContentCachingRequestWrapper wrapper =  WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
		if (wrapper != null) {
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				try {
					int length = Math.min(buf.length, 1024);
					//最大只打印1024字节
					return new String(buf, 0, length, wrapper.getCharacterEncoding());
				} catch (UnsupportedEncodingException var6) {
					return "[unknown]";
				}
			}
		}
		return "";
	}

	private boolean needToLog(Method method) {
		//GET请求不记录日志
		return !method.getDeclaringClass().equals(AllExceptionAdvice.class);
	}


	//用户访问日志记录

}
