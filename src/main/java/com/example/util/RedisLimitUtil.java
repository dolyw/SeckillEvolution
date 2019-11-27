package com.example.util;

import com.example.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * RedisLimitUtil
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019/11/14 16:44
 */
@Component
public class RedisLimitUtil {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(RedisLimitUtil.class);

    /**
     * 秒级限流(每秒限制多少请求)字符串脚本
     */
    private static String LIMIT_SECKILL_SCRIPT = null;

    /**
     * 自定义参数限流(自定义多少时间限制多少请求)字符串脚本
     */
    private static String LIMIT_CUSTOM_SCRIPT = null;

    /**
     * redis-key-前缀-limit-限流
     */
    private static final String LIMIT = "limit:";

    /**
     * redis-key-名称-limit-一个时间窗口内请求的数量累计(限流累计请求数)
     */
    private static final String LIMIT_REQUEST = "limit:request";

    /**
     * redis-key-名称-limit-一个时间窗口开始时间(限流开始时间)
     */
    private static final String LIMIT_TIME = "limit:time";

    /**
     * 构造方法初始化加载Lua脚本
     */
    public RedisLimitUtil() {
        LIMIT_SECKILL_SCRIPT = getScript("redis/limit-seckill.lua");
        LIMIT_CUSTOM_SCRIPT = getScript("redis/limit-custom.lua");
    }

    /**
     * 秒级限流判断(每秒限制多少请求)
     *
	 * @param maxRequest 限流最大请求数
     * @return boolean
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/25 17:57
     */
    public Long limit(String maxRequest) {
        // 获取key名，当前时间戳
        String key = LIMIT + String.valueOf(System.currentTimeMillis() / 1000);
        // 传入参数，限流最大请求数
        List<String> args = new ArrayList<>();
        args.add(maxRequest);
        return eval(LIMIT_SECKILL_SCRIPT, Collections.singletonList(key), args);
    }

    /**
     * 自定义参数限流判断(自定义多少时间限制多少请求)
     *
     * @param maxRequest 限流最大请求数
     * @param timeRequest 一个时间窗口(秒)
     * @return boolean
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/25 17:57
     */
    public Long limit(String maxRequest, String timeRequest) {
        // 获取key名，一个时间窗口开始时间(限流开始时间)和一个时间窗口内请求的数量累计(限流累计请求数)
        List<String> keys = new ArrayList<>();
        keys.add(LIMIT_TIME);
        keys.add(LIMIT_REQUEST);
        // 传入参数，限流最大请求数，当前时间戳，一个时间窗口时间(毫秒)(限流时间)
        List<String> args = new ArrayList<>();
        args.add(maxRequest);
        args.add(String.valueOf(System.currentTimeMillis()));
        args.add(timeRequest);
        return eval(LIMIT_CUSTOM_SCRIPT, keys, args);
    }

    /**
     * 执行Lua脚本方法
     *
     * @param script
	 * @param keys
	 * @param args
     * @return java.lang.Object
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/26 10:50
     */
    private Long eval(String script, List<String> keys, List<String> args) {
        // 执行脚本
        Object result = JedisUtil.eval(script, keys, args);
        // 结果请求数大于0说明不被限流
        return (Long) result;
    }

    /**
     * 获取Lua脚本
     *
     * @param path
     * @return java.lang.String
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/25 17:57
     */
    private static String getScript(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = RedisLimitUtil.class.getClassLoader().getResourceAsStream(path);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                stringBuilder.append(str).append(System.lineSeparator());
            }
        } catch (IOException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            throw new CustomException("获取Lua限流脚本出现问题: " + Arrays.toString(e.getStackTrace()));
        }
        return stringBuilder.toString();
    }

}
