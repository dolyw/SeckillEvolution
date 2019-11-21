package com.example.controller;

import com.example.common.ResponseBean;
import com.example.constant.Constant;
import com.example.dto.custom.StockDto;
import com.example.exception.CustomException;
import com.example.seckill.ISeckillService;
import com.example.service.ISeckillEvolutionService;
import com.example.service.IStockOrderService;
import com.example.service.IStockService;
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
    private static final Integer ITEM_STOCK_COUNT = 50;

    /**
     * 初始化卖出数量，乐观锁版本
     */
    private static final Integer ITEM_STOCK_SALE = 0;

    /**
     * 初始化库存数量
     *
     * @param
     * @return java.lang.String
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/14 16:56
     */
    @PutMapping("/init/{id}")
    public ResponseBean init(@PathVariable("id") Integer id) {
        // 更新库存表
        StockDto stockDto = new StockDto();
        stockDto.setId(id);
        stockDto.setName(Constant.ITEM_STOCK_NAME);
        stockDto.setCount(ITEM_STOCK_COUNT);
        stockDto.setSale(ITEM_STOCK_SALE);
        stockDto.setVersion(ITEM_STOCK_SALE);
        stockService.updateByPrimaryKey(stockDto);
        // 删除订单表所有数据
        stockOrderService.delete(null);
        return new ResponseBean(HttpStatus.OK.value(), "初始化库存成功", null);
    }

    /**
     * 传统方式下订单
     *
     * @param id 商品ID
     * @return com.example.common.ResponseBean
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/21 19:50
     */
    @PostMapping("/createWrongOrder/{id}")
    public ResponseBean createWrongOrder(@PathVariable("id") Integer id) throws Exception {
        Integer orderCount = seckillEvolutionService.createWrongOrder(id);
        return new ResponseBean(HttpStatus.OK.value(), "购买成功", orderCount);
    }

}
