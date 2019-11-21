/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.service.impl;


import com.example.common.impl.BaseServiceImpl;
import com.example.dao.StockDao;
import com.example.dto.custom.StockDto;
import com.example.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * StockServiceImpl
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
@Service("stockService")
public class StockServiceImpl extends BaseServiceImpl<StockDto> implements IStockService {

    @Autowired
    private StockDao stockDao;

    @Override
    public List<StockDto> findPageInfo(StockDto stockDto) {
        return stockDao.findPageInfo(stockDto);
    }
}