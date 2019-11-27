package com.example.controller;

import com.example.common.ResponseBean;
import com.example.constant.Constant;
import com.example.dto.custom.StockDto;
import com.example.dto.custom.StockOrderDto;
import com.example.exception.CustomException;
import com.example.limit.Limit;
import com.example.seckill.ISeckillService;
import com.example.service.ISeckillEvolutionService;
import com.example.service.IStockOrderService;
import com.example.service.IStockService;
import com.example.util.JedisUtil;
import com.example.util.RedisLimitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 一个简单的秒杀架构的演变
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019/11/20 19:49
 */
@RestController
@RequestMapping("/seckill")
public class SeckillEvolutionController {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SeckillEvolutionController.class);

    private final IStockService stockService;

    private final IStockOrderService stockOrderService;

    private final ISeckillEvolutionService seckillEvolutionService;

    /**
     * 构造注入
     * @param stockService
     * @param stockOrderService
     */
    @Autowired
    public SeckillEvolutionController(IStockService stockService, IStockOrderService stockOrderService,
                                      ISeckillEvolutionService seckillEvolutionService) {
        this.stockService = stockService;
        this.stockOrderService = stockOrderService;
        this.seckillEvolutionService = seckillEvolutionService;
    }

    /**
     * 初始化库存数量
     */
    private static final Integer ITEM_STOCK_COUNT = 10;

    /**
     * 初始化卖出数量，乐观锁版本
     */
    private static final Integer ITEM_STOCK_SALE = 0;

    /**
     * RedisLimitUtil
     */
    @Autowired
    private RedisLimitUtil redisLimitUtil;

    /**
     * 初始化库存数量
     *
     * @param id 商品ID
     * @return com.example.common.ResponseBean
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/22 15:59
     */
    @PutMapping("/init/{id}")
    public ResponseBean init(@PathVariable("id") Integer id) {
        // 更新库存表该商品的库存，已售，乐观锁版本号
        StockDto stockDto = new StockDto();
        stockDto.setId(id);
        stockDto.setName(Constant.ITEM_STOCK_NAME);
        stockDto.setCount(ITEM_STOCK_COUNT);
        stockDto.setSale(ITEM_STOCK_SALE);
        stockDto.setVersion(ITEM_STOCK_SALE);
        stockService.updateByPrimaryKey(stockDto);
        // 删除订单表该商品所有数据
        StockOrderDto stockOrderDto = new StockOrderDto();
        stockOrderDto.setStockId(id);
        stockOrderService.delete(stockOrderDto);
        return new ResponseBean(HttpStatus.OK.value(), "初始化库存成功", null);
    }

    /**
     * 缓存预热
     *
     * @param id 商品ID
     * @return com.example.common.ResponseBean
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/22 15:59
     */
    @PutMapping("/initCache/{id}")
    public ResponseBean initCache(@PathVariable("id") Integer id) {
        StockDto stockDto = stockService.selectByPrimaryKey(id);
        // 商品缓存预热
        JedisUtil.set(Constant.PREFIX_COUNT + id.toString(), stockDto.getCount().toString());
        JedisUtil.set(Constant.PREFIX_SALE + id.toString(), stockDto.getSale().toString());
        JedisUtil.set(Constant.PREFIX_VERSION + id.toString(), stockDto.getVersion().toString());
        return new ResponseBean(HttpStatus.OK.value(), "缓存预热成功", null);
    }

    /**
     * 传统方式下订单
     *
     * @param id 商品ID
     * @return com.example.common.ResponseBean
     * @throws Exception
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/21 19:50
     */
    @PostMapping("/createWrongOrder/{id}")
    public ResponseBean createWrongOrder(@PathVariable("id") Integer id) throws Exception {
        Integer orderCount = seckillEvolutionService.createWrongOrder(id);
        return new ResponseBean(HttpStatus.OK.value(), "购买成功", orderCount);
    }

    /**
     * 使用乐观锁下订单
     *
     * @param id 商品ID
     * @return com.example.common.ResponseBean
     * @throws Exception
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/22 14:24
     */
    @PostMapping("/createOptimisticLockOrder/{id}")
    public ResponseBean createOptimisticLockOrder(@PathVariable("id") Integer id) throws Exception {
        Integer orderCount = seckillEvolutionService.createOptimisticLockOrder(id);
        return new ResponseBean(HttpStatus.OK.value(), "购买成功", orderCount);
    }

    /**
     * 使用乐观锁下订单，并且添加读缓存，性能提升
     *
     * @param id 商品ID
     * @return com.example.common.ResponseBean
     * @throws Exception
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/22 14:24
     */
    @PostMapping("/createOptimisticLockOrderWithRedis/{id}")
    public ResponseBean createOptimisticLockOrderWithRedis(@PathVariable("id") Integer id) throws Exception {
        // 错误的，线程不安全
        // Integer orderCount = seckillEvolutionService.createOptimisticLockOrderWithRedisWrong(id);
        // 正确的，线程安全
        Integer orderCount = seckillEvolutionService.createOptimisticLockOrderWithRedisSafe(id);
        return new ResponseBean(HttpStatus.OK.value(), "购买成功", null);
    }

    /**
     * 使用乐观锁下订单，并且添加读缓存，再添加限流
     *
     * @param id 商品ID
     * @return com.example.common.ResponseBean
     * @throws Exception
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/22 14:24
     */
    @Limit
    @PostMapping("/createOptimisticLockOrderWithRedisLimit/{id}")
    public ResponseBean createOptimisticLockOrderWithRedisLimit(@PathVariable("id") Integer id) throws Exception {
        // 错误的，线程不安全
        // Integer orderCount = seckillEvolutionService.createOptimisticLockOrderWithRedisWrong(id);
        // 正确的，线程安全
        Integer orderCount = seckillEvolutionService.createOptimisticLockOrderWithRedisSafe(id);
        return new ResponseBean(HttpStatus.OK.value(), "购买成功", null);
    }

}
