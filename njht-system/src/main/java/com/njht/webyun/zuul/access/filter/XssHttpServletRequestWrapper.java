package com.njht.webyun.zuul.access.filter;


import com.njht.webyun.spring.SpringContext;
import com.njht.webyun.utils.EncryptUtil;
import com.njht.webyun.system.config.UserLoginParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * @author David
 * Xss漏洞过滤，传参密码解密
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private String[] filterChars;
    private String[] replaceChars;
    private String DECRYPT_KEY = "abcdef0123456789";

    UserLoginParam userLoginParam = SpringContext.getBean(UserLoginParam.class);

    public XssHttpServletRequestWrapper(HttpServletRequest request, String filterChar, String replaceChar, String splitChar) {
        super(request);
        if (filterChar != null && filterChar.length() > 0) {
            filterChars = filterChar.split(splitChar);
        }
        if (replaceChar != null && replaceChar.length() > 0) {
            replaceChars = replaceChar.split(splitChar);
        }
    }

    public String getQueryString() {
        String value = super.getQueryString();
        if (value != null) {
            value = xssEncode(value);
        }
        return value;
    }

    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(xssEncode(name));
        try {
            //用户名和密码在登录时通过base64加密，不利于xssEncode过滤
            if (value != null) {
                if ("username".equals(name) && userLoginParam.isDecryptParam()) {
                     value = EncryptUtil.aesDecryptByString(value, DECRYPT_KEY);
                } else if ("password".equals(name) && userLoginParam.isDecryptParam()) {
                    value = EncryptUtil.aesDecryptByString(value, DECRYPT_KEY);
                } else {
                    value = xssEncode(value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public String[] getParameterValues(String name) {
        String[] parameters = super.getParameterValues(name);
        if (parameters == null || parameters.length == 0) {
            return null;
        }
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = xssEncode(parameters[i]);
        }
        return parameters;
    }

    /**
     * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/> getHeaderNames 也可能需要覆盖
     */
    public String getHeader(String name) {

        String value = super.getHeader(xssEncode(name));
        if (value != null) {
            /* 防止在进行urldecoder时，将accpet中的xhtml+xml中的+过滤掉，从而导致报错   */
            if (value.equals(super.getHeader("Accept"))) {
                return value;
            }
            value = xssEncode(value);
        }
        return value;
    }

    /**
     * 将容易引起xss漏洞的半角字符直接替换成全角字符
     *
     * @param s
     * @return
     */
    private String xssEncode(String s) {
        if (s == null || s.equals("")) {
            return s;
        }
        try {
            //  	s = URLEncoder.encode(s, "UTF-8");
            s = URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < filterChars.length; i++) {
            if (s.contains(filterChars[i])) {
                s = s.replace(filterChars[i], replaceChars[i]);
            }
        }
        return s;
    }
}   