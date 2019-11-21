/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.service;

import com.example.common.IBaseService;
import com.example.dto.custom.StockOrderDto;
import java.util.List;

/**
 * IStockOrderService
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
public interface IStockOrderService extends IBaseService<StockOrderDto> {

    /**
     * 列表
     * @param stockOrderDto
     * @return java.util.List<com.example.dto.custom.StockOrderDto;>
     * @author wliduo[i@dolyw.com]
     * @date 2019-11-20 18:03:33
     */
    public List<StockOrderDto> findPageInfo(StockOrderDto stockOrderDto);
}