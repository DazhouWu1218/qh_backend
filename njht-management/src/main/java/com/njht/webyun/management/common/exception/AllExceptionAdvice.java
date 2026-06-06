package com.njht.webyun.management.common.exception;

import com.njht.webyun.enums.ReturnCodeEnum;
import com.njht.webyun.exception.CommonException;
import com.njht.webyun.utils.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author daiguojun
 * @date 2021-07-13 22:29
 * 产品模块全局异常处理
 */
@RestControllerAdvice(basePackages = {"com.njht.webyun.publish"})
@Slf4j
public class AllExceptionAdvice {

    /**
     * 精确匹配异常执行改方法，后续执行下面的方法
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ReturnT handlerValidException(MethodArgumentNotValidException e){
        log.error("数据校验出现问题{},异常类型{}",e.getMessage(),e.getCause());
        Map<String,String> map = new HashMap<>(16);
        //1.获取校验错误的结果
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            //获取错误属性名称以及错误的提示信息
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            map.put(field,defaultMessage);
        });
        return ReturnT.failed(ReturnCodeEnum.VALID_EXCEPTION.getCode(),ReturnCodeEnum.VALID_EXCEPTION.getMessage(),map);
    }

    @ExceptionHandler(value = {CommonException.class})
    public ReturnT handlerTokenException(CommonException e){
        return ReturnT.failed(ReturnCodeEnum.FAIL_CODE.getCode(),e.getMsg());
    }

    @ExceptionHandler(value = Throwable.class)
    public ReturnT handlerValidException(Throwable throwable){
        log.error("出现错误",throwable);
        return ReturnT.failed(ReturnCodeEnum.UNKNOWN_EXCEPTION.getCode(),ReturnCodeEnum.UNKNOWN_EXCEPTION.getMessage(),throwable.getMessage());
    }
}
