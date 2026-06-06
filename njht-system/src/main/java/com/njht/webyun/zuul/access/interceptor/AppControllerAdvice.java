package com.njht.webyun.zuul.access.interceptor;

import com.njht.webyun.common.PageRespBean;
import com.njht.webyun.common.RespBean;
import com.njht.webyun.zuul.token.TokenConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * @author David
 * @date 2021/3/16
 * @description:
 * 这是一个拦截Controller中web请求进行统一处理的类
 * 实现ResponseBodyAdvice接口实现统一数据的封装
 * 注意要设置为Object类型
 */
@ControllerAdvice
public class AppControllerAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger logger = LoggerFactory.getLogger(AppControllerAdvice.class);

    private TokenConfig tokenConfig;
    //指定处理请求方法中抛出的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handler(Exception exception) {
        return exception;
    }

    /*
    执行Controller中的web请求方法结束，返回数据到前端的时候，是否要重写响应体
    如果返回的是true就是要执行重写 反之不重写
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        String requestURI = request.getURI().getPath();
//        String[] excludePath = tokenConfig.getExcludeAuthPath().split(",");
//        boolean auth = (Arrays.stream(excludePath).filter(path-> PathUtil.isPathMatch(path,requestURI )).findFirst().isPresent());
//        if(auth){
//            return body;
//        }
        /*
            如果存在返回字符串的情况，需要返回字符串类型，否则会报错
            解决方案：body就是我们返回的数据 ,判断body对象的类型：
            如果是字符串，就返回objectmapper序列化后的字符串，否则返回统一封装的类型
         */
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (body instanceof String) {
                //判断是字符串类型将数据以json格式返回
                return objectMapper.writeValueAsString(RespBean.ok("ok",body));
            }
            if(body instanceof Exception){
                logger.error("【异常】<"+requestURI+">："+ ((Exception) body).getMessage());
                logger.error("【异常】<"+requestURI+">：",body);
                return RespBean.error(((Exception) body).getMessage());
            }
            if(body instanceof Page){
                return PageRespBean.ok("OK", body,((Page) body).getTotal());
            }
            //
            if(body instanceof RespBean || body instanceof PageRespBean){
                return body;
            }else{
                return RespBean.ok("ok",body);
            }
        }
         catch (JsonProcessingException e) {
            return RespBean.error(e.getMessage());
        }
    }
}
