/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.seckill.impl;


import com.example.dao.StockDao;
import com.example.dao.StockOrderDao;
import com.example.dto.custom.StockDto;
import com.example.dto.custom.StockOrderDto;
import com.example.exception.CustomException;
import com.example.seckill.ISeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 使用乐观锁
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
@Service("seckillOptimisticLockService")
public class SeckillOptimisticLockServiceImpl implements ISeckillService {

    @Autowired
    private StockDao stockDao;

    @Autowired
    private StockOrderDao stockOrderDao;

    @Override
    public StockDto checkStock(Integer id) {
        StockDto stockDto = stockDao.selectByPrimaryKey(id);
        if (stockDto.getCount() > 0) {
            return stockDto;
        }
        throw new CustomException("库存不足");
    }

    @Override
    public Integer saleStock(StockDto stockDto) {
        return stockDao.updateByOptimisticLock(stockDto);
    }

    @Override
    public Integer createOrder(StockDto stockDto) {
        StockOrderDto stockOrderDto = new StockOrderDto();
        stockOrderDto.setStockId(stockDto.getId());
        return stockOrderDao.insertSelective(stockOrderDto);
    }
}