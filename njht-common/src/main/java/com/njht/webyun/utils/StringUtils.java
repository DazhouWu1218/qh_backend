/*
 * 版权信息：Copyright (c) 2014, Aoto. All rights reserved.
 * 文件编号：StringUtils.java
 * 文件名称：StringUtils.java
 * 系统编号：aotoframework
 * 系统名称：aotoframework
 * 模块编号：
 * 模块名称：
 * 作          者：jiangp
 * 完成日期：2014年5月16日
 * 设计文档：<列出相关设计文档的编号、名称。>
 * 内容摘要：<说明文件包含的类、类的性质/版型、类的状态说明、主要功能、系统相关界面、包含的区段、关键词及其一般说明、文件调用等。>
 */
package com.njht.webyun.utils;

import com.njht.webyun.spring.SpringContext;
import org.springframework.context.MessageSource;

import java.util.*;

/**
 * 〈一句话功能简述〉 〈功能详细描述〉
 * 
 * @author jiangp
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （必须）
 */
public final class StringUtils extends org.apache.commons.lang3.StringUtils
{
    private static final LinkedHashMap<String, String> OS_WINDOWS;
    private static final LinkedHashMap<String, String> IE_BROWSERS;
    private static final MessageSource messageSource;

    static
    {
        messageSource = SpringContext.getBean(MessageSource.class);
        
        OS_WINDOWS = new LinkedHashMap<String, String>();
        OS_WINDOWS.put("Windows NT 6.1", "Windows 7");
        OS_WINDOWS.put("Windows NT 5.1", "Windows XP");
        OS_WINDOWS.put("Windows NT 6.3", "Windows 8.1");
        OS_WINDOWS.put("Windows NT 6.2", "Windows 8");
        OS_WINDOWS.put("Windows NT 5.2", "Windows Server 2003");
        OS_WINDOWS.put("Windows NT 5.0", "Windows 2000");
        OS_WINDOWS.put("Windows NT 6.0", "Windows Vista");

        IE_BROWSERS = new LinkedHashMap<String, String>();
        IE_BROWSERS.put("MSIE 7.0", "Internet Explorer 7");
        IE_BROWSERS.put("MSIE 8.0", "Internet Explorer 8");
        IE_BROWSERS.put("MSIE 9.0", "Internet Explorer 9");
        IE_BROWSERS.put("rv:11.0", "Internet Explorer 11");
        IE_BROWSERS.put("rv 11.0", "Internet Explorer 11");
        IE_BROWSERS.put("MSIE 10.0", "Internet Explorer 10");
        IE_BROWSERS.put("MSIE 6.0", "Internet Explorer 6");
    }

    public static String camelCaseToSnakeCase(String camel)
    {
        String separator = "_";
        String snakeCase = StringUtils.EMPTY;
        
        for (char c : camel.toCharArray())
        {
            if (c >= 65 && c <= 90)
            {
                snakeCase += separator + c;
            }
            else if (c >= 97 && c <= 122)
            {
                snakeCase += (char)(c - 32);
            }
            else
            {
                snakeCase += c;
            }
        }

        return snakeCase;
    }
    
    public static String snakeCaseToCamelCase(String snake)
    {
        String camel = StringUtils.EMPTY;
        char separator = '_';
        boolean upper = false;

        for (char c : snake.toCharArray())
        {
            if (c >= 65 && c <= 90)
            {
                camel += (upper ? c : (char)(c + 32));
                upper = false;
            }
            else if (c >= 97 && c <= 122)
            {
                camel += (upper ? (char)(c - 32) : c);
                upper = false;
            }
            else if (separator == c)
            {
                upper = true;
            }
            else
            {
                camel += c;
            }
        }

        return camel;
    }

    public static String[] AnalyzeUserAgent(String userAgent)
    {
        String os = null;
        String browser = null;
        Iterator<Map.Entry<String, String>> iterator = OS_WINDOWS.entrySet().iterator();
        Map.Entry<String, String> entry = null;

        while (iterator.hasNext())
        {
            entry = iterator.next();

            if (StringUtils.containsIgnoreCase(userAgent, entry.getKey()))
            {
                os = entry.getValue();
                break;
            }
        }

        iterator = IE_BROWSERS.entrySet().iterator();

        while (iterator.hasNext())
        {
            entry = iterator.next();

            if (StringUtils.containsIgnoreCase(userAgent, entry.getKey()))
            {
                browser = entry.getValue();
                break;
            }
        }

        if (StringUtils.isEmpty(browser))
        {
            int index = StringUtils.indexOfIgnoreCase(userAgent, "Chrome");
            String browserVersion = null;

            if (-1 != index)
            {
                int end = StringUtils.indexOfIgnoreCase(userAgent, " ", index);
                browserVersion = StringUtils.substring(userAgent, index + 7, end);
                browser = "Chrome " + trim(browserVersion);
            }
            else
            {
                index = StringUtils.indexOfIgnoreCase(userAgent, "Firefox");

                if (-1 != index)
                {
                    browserVersion = StringUtils.substring(userAgent, index + 8);
                    browser = "Firefox " + trim(browserVersion);
                }
            }
        }

        int length = StringUtils.length(os);

        if (length > 32)
        {
            os = StringUtils.substring(os, 0, 32);
        }

        length = StringUtils.length(browser);

        if (length > 32)
        {
            browser = StringUtils.substring(browser, 0, 32);
        }

        return new String[] { os, browser };
    }
    
    public static String getAction(String code, String... args)
    {
        String[] params = null;
        
        if (StringUtils.isEmpty(code))
        {
            return StringUtils.EMPTY;
        }
        
        if (null != args && args.length > 0)
        {
            List<String> list = new ArrayList<String>(2);
            
            for (String str : args)
            {
                list.add(messageSource.getMessage(str, null, Locale.CHINESE));
            }
            
            params = new String[list.size()];
            list.toArray(params);
        }
        
        return messageSource.getMessage(code, params, Locale.CHINESE);
    }

    public static void main(String[] args)
    {
        String a = StringUtils.defaultIfEmpty("", StringUtils.EMPTY);
        System.out.println(a);
//         String a = camelCaseToSnakeCase("lastUpdatedDate");
//         System.out.println(a);

//        String ie11 = "Mozilla/5.0 (Windows NT 6.3; Trident/5.0; rv 11.0)";
//        String ie10 = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/5.0)";
//        String ie9 = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)";
//        String ie8 = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322)";
//        String ie7 = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2; .NET CLR 1.1.4322)";
//        String ie6 = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322)";
//        String aoyou = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322; Maxthon 2.0)";
//        String qq = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322) QQBrowser/6.8.10793.201";
//        String green = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322; GreenBrowser)";
//        String se360 = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322; 360SE)";
//
//        String chrome = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36";
//        String safari = "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN) AppleWebKit/533.17.8 (KHTML, like Gecko) Version/5.0.1 Safari/533.17.8";
//        String fireFox = "Mozilla/5.0 (Windows NT 5.2; rv:7.0.1) Gecko/20100101 Firefox/7.0.1";
//        String opera = "Opera/9.80  (Windows NT 5.2; U; zh-cn) Presto/2.9.168 Version/11.51";
//        String other = "(Windows NT 5.2; U; zh-cn) Presto/2.9.168 Version/11.51";
//
//        String[] aa = AnalyzeUserAgent(chrome);
//        System.out.println(aa[0]);
//        System.out.println(aa[1]);
    }
}
