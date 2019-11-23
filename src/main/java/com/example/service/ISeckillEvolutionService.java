/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.service;


/**
 * ISeckillEvolutionService
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
public interface ISeckillEvolutionService {

    /**
     * 传统方式的创建订单(并发会出现错误)
     *
     * @param id
     * @return java.lang.Integer
     * @throws Exception
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/22 14:21
     */
    Integer createWrongOrder(Integer id) throws Exception;

    /**
     * 使用乐观锁创建订单(解决卖超问题)
     *
     * @param id
     * @return java.lang.Integer
     * @throws Exception
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/22 14:21
     */
    Integer createOptimisticLockOrder(Integer id) throws Exception;

    /**
     * 使用乐观锁创建订单(解决卖超问题)，加缓存读(线程不安全)，提升性能，
     *
     * @param id
     * @return java.lang.Integer
     * @throws Exception
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/22 14:21
     */
    Integer createOptimisticLockOrderWithRedisWrong(Integer id) throws Exception;

    /**
     * 使用乐观锁创建订单(解决卖超问题)，加缓存读(线程安全)，提升性能，
     *
     * @param id
     * @return java.lang.Integer
     * @throws Exception
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/22 14:21
     */
    Integer createOptimisticLockOrderWithRedisSafe(Integer id) throws Exception;

}