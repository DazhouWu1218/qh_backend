package com.njht.webyun.zuul.session;

import javax.servlet.SessionCookieConfig;

/**
 *
 * 补充 SessionCookie 中未定义的配置项
 * @author David
 */
public interface MySessionCookieConfig extends SessionCookieConfig {

    String getDomainPattern();

    void setDomainPattern(String domainPattern);

    String getJvmRoute();

    void setJvmRoute(String jvmRoute);

    boolean isUseBase64Encoding();

    void setUseBase64Encoding(boolean useBase64Encoding);

}
