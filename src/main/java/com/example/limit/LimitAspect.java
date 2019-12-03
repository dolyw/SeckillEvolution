package com.example.limit;

import com.example.exception.CustomException;
import com.example.util.RedisLimitUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
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
     * 一个时间窗口时间(毫秒)(限流时间)
     */
    private static final String TIME_REQUEST = "1000";

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
        Long maxRequest = 0L;
        // 一个时间窗口(毫秒)为1000的话默认调用秒级限流判断(每秒限制多少请求)
        if (TIME_REQUEST.equals(limit.timeRequest())) {
            maxRequest = redisLimitUtil.limit(limit.maxRequest());
        } else {
            maxRequest = redisLimitUtil.limit(limit.maxRequest(), limit.timeRequest());
        }
        // 返回请求数量大于0说明不被限流
        if (maxRequest > 0) {
            // 放行，执行后续方法
            try {
                result = proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                throw new CustomException(throwable.getMessage());
            }
        } else {
            // 直接返回响应结果
            throw new CustomException("请求拥挤，请稍候重试");
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
