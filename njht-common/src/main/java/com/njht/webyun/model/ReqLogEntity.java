package com.njht.webyun.model;

/**
 * api 请求日志的实体类
 */
public class ReqLogEntity {
    private String requestId; // 请求唯一ID
    private String requestIp;
    private String method;
    private String paramData;
    private String bodyData;
    private String uri;
    private String requestName;
    private String sessionId;
    private String serverName;

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParamData() {
        return paramData;
    }

    public void setParamData(String paramData) {
        this.paramData = paramData;
    }

    public String getBodyData() {
        return bodyData;
    }

    public void setBodyData(String bodyData) {
        this.bodyData = bodyData;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "ReqLogEntity{" +
                "requestId='" + requestId + '\'' +
                ", requestIp='" + requestIp + '\'' +
                ", method='" + method + '\'' +
                ", paramData='" + paramData + '\'' +
                ", bodyData='" + bodyData + '\'' +
                ", uri='" + uri + '\'' +
                ", requestName='" + requestName + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", serverName='" + serverName + '\'' +
                '}';
    }
}