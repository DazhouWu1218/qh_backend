package com.njht.webyun.i18n;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author chengqh
 * @Date 2020/11/19
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(LocalConfig.class)
public @interface EnableI18n {
}
