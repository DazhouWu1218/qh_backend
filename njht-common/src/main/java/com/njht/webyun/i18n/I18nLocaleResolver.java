package com.njht.webyun.i18n;

import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class I18nLocaleResolver implements LocaleResolver {
    private static final String LANG = "lang";
    private static final String LANG_SESSION = "lang_session";


    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        String lang = httpServletRequest.getHeader(LANG);
        Locale locale = Locale.getDefault();
        if (lang != null && lang != "") {
            String[] langueage = lang.split("_");
            locale = new Locale(langueage[0], langueage[1]);
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute(LANG_SESSION, locale);
        } else {
            HttpSession session = httpServletRequest.getSession();
            Locale localeInSession = (Locale) session.getAttribute(LANG_SESSION);
            if (localeInSession != null) {
                locale = localeInSession;
            }
        }
        return locale;
    }

    /**
     * export 表的时候，是由a标签直接请求的，header中没有lang参数
     * @param httpServletRequest
     * @param lang
     * @return
     */
    public Locale resolveLocale(HttpServletRequest httpServletRequest,String lang) {
        Locale locale = Locale.getDefault();
        if (lang != null && lang != "") {
            String[] langueage = lang.split("_");
            locale = new Locale(langueage[0], langueage[1]);
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute(LANG_SESSION, locale);
        } else {
            HttpSession session = httpServletRequest.getSession();
            Locale localeInSession = (Locale) session.getAttribute(LANG_SESSION);
            if (localeInSession != null) {
                locale = localeInSession;
            }
        }
        return locale;
    }


    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
