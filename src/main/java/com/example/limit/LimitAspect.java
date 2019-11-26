package com.example.limit;

import com.alibaba.fastjson.JSON;
import com.example.common.ResponseBean;
import com.example.exception.CustomException;
import com.example.util.RedisLimitUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * LimitAspect限流切面
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019/11/26 10:07
 */
@Order(0)
@Aspect
@Component
public class LimitAspect {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(LimitAspect.class);

    /**
     * RedisLimitUtil
     */
    @Autowired
    private RedisLimitUtil redisLimitUtil;

    /**
     * 对应注解
     *
     * @param
     * @return void
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/26 10:11
     */
    @Pointcut("@annotation(com.example.limit.Limit)")
    public void aspect() {}

    /**
     * 切面
     *
     * @param proceedingJoinPoint
     * @return java.lang.Object
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/26 10:11
     */
    @Around("aspect() && @annotation(limit)")
    public Object Interceptor(ProceedingJoinPoint proceedingJoinPoint, Limit limit) {
        Object result = null;
        try {
            // 返回请求数量大于0说明不被限流
            Long maxRequest = redisLimitUtil.limit(limit.path(), limit.maxRequest(), limit.timeRequest());
            logger.info(maxRequest.toString());
            if (maxRequest > 0) {
                // 放行，执行后续方法
                result = proceedingJoinPoint.proceed();
            } else {
                // 直接返回响应结果
                return JSON.toJSONString(new ResponseBean(HttpStatus.OK.value(), "请求拥挤，请稍候重试", null));
            }
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage());
            throw new CustomException("限流切面执行出现问题: " + throwable.getMessage());
        }
        return result;
    }

    /**
     * 执行方法前再执行
     *
     * @param limit
     * @return void
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/26 10:10
     */
    @Before("aspect() && @annotation(limit)")
    public void before(Limit limit) {
        // logger.info("before");
    }

    /**
     * 执行方法后再执行
     *
     * @param limit
     * @return void
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/26 10:10
     */
    @After("aspect() && @annotation(limit)")
    public void after(Limit limit) {
        // logger.info("after");
    }

}
