/*
 * 版权信息：Copyright (c) 2020, Aoto. All rights reserved.
 */
package com.njht.webyun.exception;

/**
 * 文件编号：
 * 文件名称：CommMsgException.java
 * 系统编号：framework-commons
 * 系统名称：framework-commons
 * 模块编号：
 * 模块名称：
 * 作者：
 * 完成日期：
 * 设计文档：<列出相关设计文档的编号、名称。>
 * 内容摘要：自定义api的通用异常信息类
 */
public class CommMsgException extends RuntimeException {

    private static final long serialVersionUID = 8013836415766229915L;
    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @author
     */
    public CommMsgException() {super();}
    
    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @author 
     */
    public CommMsgException(String msg) {
        super(msg);
    }

    /**
     * 两个参数，可传递原始异常栈信息
     * @param message
     * @param cause
     */
    public CommMsgException(String message, Throwable cause)
    {
        super(message, cause);
    }


    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @author 
     * @param cause Throwable
     */
    public CommMsgException(Throwable cause)
    {
        super(cause);
    }
}
