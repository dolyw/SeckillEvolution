package com.example.controller;

import com.example.exception.CustomException;
import com.example.util.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Redis悲观锁和乐观锁测试
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019/11/13 19:27
 */
@RestController
@RequestMapping("/redis")
public class RedisLockController {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(RedisLockController.class);

    /**
     * 记录实际卖出的商品数量
     */
    private AtomicInteger successNum = new AtomicInteger(0);

    /**
     * 初始化库存数量缓存key
     */
    private static final String ITEM_STOCK = "item_stock";

    /**
     * 初始化库存数量
     */
    private static final String ITEM_STOCK_NUM = "30";

    /**
     * 获取卖出的商品数量
     *
     * @param
     * @return java.lang.String
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/14 16:19
     */
    @GetMapping
    public String index() {
        return "卖出的商品数量: " + successNum.get() + "<br/>Redis剩余的商品数量: " + JedisUtil.get(ITEM_STOCK);
    }

    /**
     * 初始化库存数量
     *
     * @param
     * @return java.lang.String
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/14 16:56
     */
    @GetMapping("/init")
    public String init() {
        // 初始化库存数量
        JedisUtil.set(ITEM_STOCK, ITEM_STOCK_NUM);
        // 初始化实际卖出的商品数量0
        successNum.set(0);
        return "初始化库存成功";
    }

    /**
     * 会出现超卖情况的减少库存方式(典型的读后写，不可重复读)
     * https://www.jianshu.com/p/380ebb7c0847
     * 两个线程同时读取到库存为10，这样两线程计算写入后库存数值都为9，而卖出的数量为2
     * 就是超卖问题出现了，正常库存应该是8
     *
     * @param
     * @return java.lang.String
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/14 17:01
     */
    @GetMapping(value = "/buy")
    public String buy() throws Exception {
        if (!JedisUtil.exists(ITEM_STOCK)) {
            throw new CustomException("库存Key在Redis不存在，请先初始化(缓存预热)");
        }
        Integer stock = Integer.parseInt(JedisUtil.get(ITEM_STOCK));
        // 读取数据后暂停10ms，出现问题的概率增大
        Thread.sleep(10);
        if (stock < 1) {
            return "库存不足";
        }
        stock = stock - 1;
        JedisUtil.set(ITEM_STOCK, stock.toString());
        return "减少库存成功，共减少" + successNum.incrementAndGet();
    }

    /**
     * 原子的减少库存方式(也会读后写，不可重复读，出现超卖问题)
     * https://www.jianshu.com/p/380ebb7c0847
     * 三个线程同时读取到库存为1时，这样两线程都穿过了if判断执行了decr操作
     * 而导致卖出数量多2份，且redis存储的库存为-2，原子操作导致减少库存都会执行
     *
     * @param
     * @return java.lang.String
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/14 17:01
     */
    @GetMapping(value = "/buy2")
    public String buy2() throws Exception {
        if (!JedisUtil.exists(ITEM_STOCK)) {
            throw new CustomException("库存Key在Redis不存在，请先初始化(缓存预热)");
        }
        Integer stock = Integer.parseInt(JedisUtil.get(ITEM_STOCK));
        // 读取数据后暂停10ms，出现问题的概率增大
        Thread.sleep(10);
        if (stock < 1) {
            return "库存不足";
        }
        // 原子操作减一
        JedisUtil.decr(ITEM_STOCK);
        return "减少库存成功，共减少" + successNum.incrementAndGet();
    }

    /**
     * 添加事务的减少库存方式(乐观锁)
     *
     * @param
     * @return java.lang.String
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/14 17:01
     */
    @GetMapping(value = "/buyTr")
    public String buyTr() {
        if (!JedisUtil.exists(ITEM_STOCK)) {
            throw new CustomException("库存Key在Redis不存在，请先初始化(缓存预热)");
        }
        Transaction transaction = null;
        try (Jedis jedis = JedisUtil.getJedis()) {
            // watch监视一个key，当事务执行之前这个key发生了改变，事务会被打断
            jedis.watch(ITEM_STOCK);
            Integer stock = Integer.parseInt(jedis.get(ITEM_STOCK));
            // 读取数据后暂停10ms，出现问题的概率增大
            Thread.sleep(10);
            if (stock > 0) {
                transaction = jedis.multi();
                stock = stock - 1;
                transaction.set(ITEM_STOCK, stock.toString());
                // 执行exec后就会自动执行jedis.unwatch()操作
                List<Object> result = transaction.exec();
                if (result == null || result.isEmpty()) {
                    // 可能是watch-key被外部修改，或者是数据操作被驳回
                    System.out.println("Transaction error");
                    // watch-key被外部修改时，transaction.discard()操作会被自动触发
                    return "Transaction error";
                }
            } else {
                jedis.unwatch();
                return "库存不足";
            }
            return "减少库存成功，共减少" + successNum.incrementAndGet();
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (transaction != null) {
                transaction.discard();
            }
            return "fail";
        }
    }

}
