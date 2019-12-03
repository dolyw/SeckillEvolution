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

import java.util.List;

/**
 * 使用乐观锁加缓存的方式(线程安全)(使用Redis批量操作mget和mset具有原子性)
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
@Service("seckillOptimisticLockRedisSafeService")
public class SeckillOptimisticLockRedisSafeServiceImpl implements ISeckillService {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SeckillOptimisticLockRedisSafeServiceImpl.class);

    @Autowired
    private StockDao stockDao;

    @Autowired
    private StockOrderDao stockOrderDao;

    @Override
    public StockDto checkStock(Integer id) throws Exception {
        // 使用缓存读取库存，减轻DB压力，Redis批量操作(具有原子性)解决线程安全问题
        List<String> dataList = JedisUtil.mget(Constant.PREFIX_COUNT + id,
                Constant.PREFIX_SALE + id, Constant.PREFIX_VERSION + id);
        Integer count = Integer.parseInt(dataList.get(0));
        Integer sale = Integer.parseInt(dataList.get(1));
        Integer version = Integer.parseInt(dataList.get(2));
        if (count > 0) {
            // 还有库存
            StockDto stockDto = new StockDto();
            stockDto.setId(id);
            stockDto.setCount(count);
            stockDto.setSale(sale);
            stockDto.setVersion(version);
            Thread.sleep(10);
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
            // 更新缓存，这里更新需要保证三个数据(库存，已售，乐观锁版本号)的一致性，使用mset原子操作
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
        Integer count = stockDto.getCount() - 1;
        Integer sale = stockDto.getSale() + 1;
        Integer version = stockDto.getVersion() + 1;
        JedisUtil.mset(Constant.PREFIX_COUNT + stockDto.getId(), count.toString(),
                Constant.PREFIX_SALE + stockDto.getId(), sale.toString(),
                Constant.PREFIX_VERSION + stockDto.getId(), version.toString());
    }

    @Override
    public Integer createOrder(StockDto stockDto) {
        StockOrderDto stockOrderDto = new StockOrderDto();
        stockOrderDto.setStockId(stockDto.getId());
        return stockOrderDao.insertSelective(stockOrderDto);
    }

}