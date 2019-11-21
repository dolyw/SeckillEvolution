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
     * 传统方式(名称注入SeckillTraditionServiceImpl)
     */
    @Autowired
    @Qualifier("seckillTraditionService")
    private ISeckillService seckillTraditionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createWrongOrder(Integer id) {
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
}