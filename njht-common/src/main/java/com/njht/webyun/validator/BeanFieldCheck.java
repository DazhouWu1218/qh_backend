package com.njht.webyun.validator;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * @author tianjm
 * @date 2021/3/1
 * @description：对Bean的字段进行校验并返回结果
 */
public class BeanFieldCheck {

    public static String check(BindingResult result){
        List<FieldError> fieldErrors = result.getFieldErrors();
        if(!fieldErrors.isEmpty()){
            return fieldErrors.get(0).getField() + fieldErrors.get(0).getDefaultMessage();
        }
        return "";
    }

}
