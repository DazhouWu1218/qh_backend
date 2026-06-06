package com.njht.webyun.enums;

/**
 * @author daiguojun
 * @date 2021-07-13 22:29
 * 产品模块枚举类，状态码的定义使用
 */
public enum ReturnCodeEnum {

    //枚举提前准备好数据类型
    UNKNOWN_EXCEPTION(500,"调用异常"),
    VALID_EXCEPTION(401,"参数格式校验异常"),
    IMG_EXCEPTION(401,"上传图片大小超出限制"),
    IMG_TYPE_EXCEPTION(401,"上传图片格式不正确"),
    IMG_UPLOAD_EXCEPTION(500,"文件上传失败"),
    VALID_CONTENT_EXCEPTION(401,"文章内容不能为空"),
    LOGIN_EXCEPTION(-1,"未登录"),
    FAIL_CODE(500,"failed"),
    SUCCESS_CODE(200,"success");

    private Integer code;
    private String message;

    ReturnCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
