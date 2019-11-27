package com.example.limit;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019/11/26 9:59
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Limit {

    /**
     * 限流最大请求数
     * @return
     */
    String maxRequest() default "10";

    /**
     * 一个时间窗口(毫秒)
     * @return
     */
    String timeRequest() default "1000";

}
