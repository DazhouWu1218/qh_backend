package com.njht.webyun.model;

/**
 * api 响应日志的实体类
 */
public class RespLogEntity {
    private String requestId; // 请求唯一ID
    private String requestIp;
    private String resultData;
    private String sessionId;
    private String uri;
    private Long consumeTime;

    @Override
    public String toString() {
        return "RespLogEntity{" +
                "requestId='" + requestId + '\'' +
                ", requestIp='" + requestIp + '\'' +
                ", resultData='" + resultData + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", uri='" + uri + '\'' +
                ", consumeTime=" + consumeTime + "ms" +
                '}';
    }

    public Long getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Long consumeTime) {
        this.consumeTime = consumeTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}