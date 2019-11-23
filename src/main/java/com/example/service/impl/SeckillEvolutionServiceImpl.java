/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.service.impl;


import com.example.dto.custom.StockDto;
import com.example.exception.CustomException;
import com.example.seckill.ISeckillService;
import com.example.service.ISeckillEvolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * StockServiceImpl
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
@Service("seckillEvolutionService")
public class SeckillEvolutionServiceImpl implements ISeckillEvolutionService {

    /**
     * 传统方式(名称注入)
     */
    @Autowired
    @Qualifier("seckillTraditionService")
    private ISeckillService seckillTraditionService;

    /**
     * 乐观锁方式(名称注入)
     */
    @Autowired
    @Qualifier("seckillOptimisticLockService")
    private ISeckillService seckillOptimisticLockServiceImpl;

    /**
     * 乐观锁加缓存方法(名称注入)，线程不安全
     */
    @Autowired
    @Qualifier("seckillOptimisticLockRedisWrongService")
    private ISeckillService SeckillOptimisticLockRedisWrongServiceImpl;

    /**
     * 乐观锁加缓存方法(名称注入)，线程安全
     */
    @Autowired
    @Qualifier("seckillOptimisticLockRedisSafeService")
    private ISeckillService seckillOptimisticLockRedisSafeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createWrongOrder(Integer id) throws Exception {
        // 检查库存
        StockDto stockDto = seckillTraditionService.checkStock(id);
        // 扣库存
        Integer saleCount = seckillTraditionService.saleStock(stockDto);
        if (saleCount <= 0) {
            throw new CustomException("扣库存失败");
        }
        // 下订单
        Integer orderCount = seckillTraditionService.createOrder(stockDto);
        if (saleCount <= 0) {
            throw new CustomException("下订单失败");
        }
        return orderCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createOptimisticLockOrder(Integer id) throws Exception {
        // 检查库存
        StockDto stockDto = seckillOptimisticLockServiceImpl.checkStock(id);
        Thread.sleep(10);
        // 扣库存
        Integer saleCount = seckillOptimisticLockServiceImpl.saleStock(stockDto);
        if (saleCount <= 0) {
            throw new CustomException("扣库存失败");
        }
        // 下订单
        Integer orderCount = seckillOptimisticLockServiceImpl.createOrder(stockDto);
        if (saleCount <= 0) {
            throw new CustomException("下订单失败");
        }
        return orderCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createOptimisticLockOrderWithRedisWrong(Integer id) throws Exception {
        // 检查库存
        StockDto stockDto = SeckillOptimisticLockRedisWrongServiceImpl.checkStock(id);
        // 扣库存
        Integer saleCount = SeckillOptimisticLockRedisWrongServiceImpl.saleStock(stockDto);
        if (saleCount <= 0) {
            throw new CustomException("扣库存失败");
        }
        Thread.sleep(10);
        // 下订单
        Integer orderCount = SeckillOptimisticLockRedisWrongServiceImpl.createOrder(stockDto);
        if (saleCount <= 0) {
            throw new CustomException("下订单失败");
        }
        return orderCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createOptimisticLockOrderWithRedisSafe(Integer id) throws Exception {
        // 检查库存
        StockDto stockDto = seckillOptimisticLockRedisSafeService.checkStock(id);
        // 扣库存
        Integer saleCount = seckillOptimisticLockRedisSafeService.saleStock(stockDto);
        if (saleCount <= 0) {
            throw new CustomException("扣库存失败");
        }
        Thread.sleep(10);
        // 下订单
        Integer orderCount = seckillOptimisticLockRedisSafeService.createOrder(stockDto);
        if (saleCount <= 0) {
            throw new CustomException("下订单失败");
        }
        Thread.sleep(10);
        return orderCount;
    }
}