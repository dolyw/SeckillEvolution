/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.service;

import com.example.common.IBaseService;
import com.example.dto.custom.StockDto;
import java.util.List;

/**
 * IStockService
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
public interface IStockService extends IBaseService<StockDto> {

    /**
     * 列表
     * @param stockDto
     * @return java.util.List<com.example.dto.custom.StockDto;>
     * @author wliduo[i@dolyw.com]
     * @date 2019-11-20 18:03:33
     */
    public List<StockDto> findPageInfo(StockDto stockDto);
}