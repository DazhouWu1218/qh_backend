package com.htht.job.core.biz.model;

import java.io.Serializable;

/**
 * common return
 * @author piesat 2015-12-4 16:32:31
 * @param <T>
 */
public class ReturnT<T> implements Serializable {
	public static final long serialVersionUID = 42L;

	public static final int SUCCESS_CODE = 200;
	public static final int SUCCESS_CODE_NODATA = 201;
	public static final int FAIL_CODE = 500;

	public static final ReturnT<String> SUCCESS = new ReturnT<String>(null);
	public static final ReturnT<String> FAIL = new ReturnT<String>(FAIL_CODE, null);

	private int code;
	private String msg;
	private T data;

	public ReturnT(){}
	public ReturnT(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public ReturnT(T data) {
		this.code = SUCCESS_CODE;
		this.data = data;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	public static <T> ReturnT<T> success() {
		ReturnT<T> returnT = new ReturnT<T>();
		returnT.setCode(SUCCESS_CODE);
		returnT.setMsg("success");
		return returnT;
	}

	public static <T> ReturnT<T> success(T data) {
		ReturnT<T> returnT = new ReturnT<T>();
		returnT.setCode(SUCCESS_CODE);
		returnT.setMsg("success");
		returnT.setData(data);
		return returnT;
	}

	public static <T> ReturnT<T> failed() {
		ReturnT<T> returnT = new ReturnT<T>();
		returnT.setCode(FAIL_CODE);
		returnT.setMsg("failed");
		return returnT;
	}

	public static <T> ReturnT<T> failed(T data) {
		ReturnT<T> returnT = new ReturnT<T>();
		returnT.setCode(FAIL_CODE);
		returnT.setMsg("failed");
		returnT.setData(data);
		return returnT;
	}

	@Override
	public String toString() {
		return "ReturnT [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}

}
