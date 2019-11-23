/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.seckill.impl;


import com.example.constant.Constant;
import com.example.dao.StockDao;
import com.example.dao.StockOrderDao;
import com.example.dto.custom.StockDto;
import com.example.dto.custom.StockOrderDto;
import com.example.exception.CustomException;
import com.example.seckill.ISeckillService;
import com.example.util.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * 使用乐观锁加缓存的方式(线程不安全)
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
@Service("seckillOptimisticLockRedisWrongService")
public class SeckillOptimisticLockRedisWrongServiceImpl implements ISeckillService {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SeckillOptimisticLockRedisWrongServiceImpl.class);

    @Autowired
    private StockDao stockDao;

    @Autowired
    private StockOrderDao stockOrderDao;

    @Override
    public StockDto checkStock(Integer id) throws Exception {
        // 使用缓存读取库存，减轻DB压力，这里会出现数据不一致
        Integer count = Integer.parseInt(JedisUtil.get(Constant.PREFIX_COUNT + id));
        Thread.sleep(100);
        Integer sale = Integer.parseInt(JedisUtil.get(Constant.PREFIX_SALE + id));
        Thread.sleep(100);
        // 第一个线程和第二个线程同时读取缓存count时，都读取到10，然后第二个线程暂停了，第一个线程继续执行，
        // 读取的version版本号为0，继续执行到已经秒杀完成，更新缓存(version版本号加一，变成1)，
        // 现在第二个线程才恢复继续执行，结果读取缓存version版本号为1(本来应该也是0)
        Integer version = Integer.parseInt(JedisUtil.get(Constant.PREFIX_VERSION + id));
        if (count > 0) {
            // 还有库存
            StockDto stockDto = new StockDto();
            stockDto.setId(id);
            stockDto.setCount(count);
            stockDto.setSale(sale);
            stockDto.setVersion(version);
            return stockDto;
        }
        throw new CustomException("库存不足");
    }

    @Override
    public Integer saleStock(StockDto stockDto) throws Exception {
        Integer saleCount = stockDao.updateByOptimisticLock(stockDto);
        // 操作数据大于0，说明扣库存成功
        if (saleCount > 0) {
            logger.info("版本号:{} {} {}", stockDto.getCount(), stockDto.getSale(), stockDto.getVersion());
            Thread.sleep(10);
            // 更新缓存，这里更新需要保证三个数据(库存，已售，乐观锁版本号)的一致性，使用Redis事务
            updateCache(stockDto);
        }
        return saleCount;
    }

    /**
     * 这里遵循先更新数据库，再更新缓存，详细的数据库与缓存一致性解析可以查看
     * https://note.dolyw.com/cache/00-DataBaseConsistency.html
     *
     * @param stockDto
     * @return java.lang.Integer
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/22 16:25
     */
    public void updateCache(StockDto stockDto) {
        // 获取事务
        Transaction transaction = null;
        try (Jedis jedis = JedisUtil.getJedis()) {
            // watch监视key，当事务执行之前这个key发生了改变，事务会被打断
            jedis.watch(Constant.PREFIX_COUNT + stockDto.getId(),
                    Constant.PREFIX_SALE + stockDto.getId(),
                    Constant.PREFIX_VERSION + stockDto.getId());
            // 开始事务
            transaction = jedis.multi();
            // 原子操作库存减一
            transaction.decr(Constant.PREFIX_COUNT + stockDto.getId());
            // 原子操作已售数和乐观锁版本号加一
            transaction.incr(Constant.PREFIX_SALE + stockDto.getId());
            transaction.incr(Constant.PREFIX_VERSION + stockDto.getId());
            // 执行exec后就会自动执行jedis.unwatch()操作
            List<Object> result = transaction.exec();
            if (result == null || result.isEmpty()) {
                // 事务失败了，可能是watch-key被外部修改，或者是数据操作被驳回
                logger.error("更新缓存失败: {}", stockDto.toString());
                // watch-key被外部修改时，transaction.discard()操作会被自动触发
            }
        } catch (Exception e) {
            logger.error("更新缓存失败: {}，异常原因: {}", stockDto.toString(), e.getMessage());
            if (transaction != null) {
                // 关闭事务
                transaction.discard();
            }
        }
    }

    @Override
    public Integer createOrder(StockDto stockDto) {
        StockOrderDto stockOrderDto = new StockOrderDto();
        stockOrderDto.setStockId(stockDto.getId());
        return stockOrderDao.insertSelective(stockOrderDto);
    }

}