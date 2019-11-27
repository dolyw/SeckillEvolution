package com.example.controller;

import com.example.limit.Limit;
import com.example.util.RedisLimitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 *  计数器(固定时间窗口)限流接口测试
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019/11/24 19:27
 */
@RestController
@RequestMapping("/limit")
public class LimitController {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(LimitController.class);

    /**
     * 一个时间窗口内最大请求数(限流最大请求数)
     */
    private static final Long MAX_NUM_REQUEST = 2L;

    /**
     * 一个时间窗口时间(毫秒)(限流时间)
     */
    private static final Long TIME_REQUEST = 5000L;

    /**
     * 一个时间窗口内请求的数量累计(限流请求数累计)
     */
    private AtomicInteger requestNum = new AtomicInteger(0);

    /**
     * 一个时间窗口开始时间(限流开始时间)
     */
    private AtomicLong requestTime = new AtomicLong(System.currentTimeMillis());

    /**
     * RedisLimitUtil
     */
    @Autowired
    private RedisLimitUtil redisLimitUtil;

    /**
     * 计数器(固定时间窗口)请求接口
     *
     * @param
     * @return java.lang.String
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/25 16:19
     */
    @GetMapping
    public String index() {
        long nowTime = System.currentTimeMillis();
        // 判断是在当前时间窗口(限流开始时间)
        if (nowTime < requestTime.longValue() + TIME_REQUEST) {
            // 判断当前时间窗口请求内是否限流最大请求数
            if (requestNum.longValue() < MAX_NUM_REQUEST) {
                // 在时间窗口内且请求数量还没超过最大，请求数加一
                requestNum.incrementAndGet();
                logger.info("请求成功，当前请求是{}次", requestNum.intValue());
                return "请求成功，当前请求是" + requestNum.intValue() + "次";
            }
        } else {
            // 超时后重置(开启一个新的时间窗口)
            requestTime = new AtomicLong(nowTime);
            requestNum = new AtomicInteger(0);
        }
        logger.info("请求失败，被限流");
        return "请求失败，被限流";
    }

    /**
     * 计数器(固定时间窗口)请求接口(限流工具类实现)
     *
     * @param
     * @return java.lang.String
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/25 18:02
     */
    @GetMapping("/redis")
    public String redis() {
        Long maxRequest = redisLimitUtil.limit(MAX_NUM_REQUEST.toString());
        // 结果请求数大于0说明不被限流
        if (maxRequest > 0) {
            logger.info("请求成功，当前请求是{}次", maxRequest);
            return "请求成功，当前请求是" + maxRequest + "次";
        }
        logger.info("请求失败，被限流");
        return "请求拥挤，请稍候重试";
    }

    /**
     * 计数器(固定时间窗口)请求接口(限流注解实现)
     *
     * @param
     * @return java.lang.String
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/26 9:46
     */
    @Limit(maxRequest = "2", timeRequest = "3000")
    @GetMapping("/annotation")
    public String annotation() {
        logger.info("请求成功");
        return "请求成功";
    }

}
