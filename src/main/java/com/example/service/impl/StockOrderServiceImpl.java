/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.service.impl;

import com.example.common.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.StockOrderDao;
import com.example.dto.custom.StockOrderDto;
import com.example.service.IStockOrderService;
import java.util.List;

/**
 * StockOrderServiceImpl
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
@Service("stockOrderService")
public class StockOrderServiceImpl extends BaseServiceImpl<StockOrderDto> implements IStockOrderService {

    @Autowired
    private StockOrderDao stockOrderDao;

    @Override
    public List<StockOrderDto> findPageInfo(StockOrderDto stockOrderDto) {
        return stockOrderDao.findPageInfo(stockOrderDto);
    }
}