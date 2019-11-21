/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.dao;

import com.example.dto.custom.StockOrderDto;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

/**
 * StockOrderDao
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
@Repository
public interface StockOrderDao extends Mapper<StockOrderDto> {

    /**
     * 列表
     * @param stockOrderDto
     * @return java.util.List<com.example.dto.custom.StockOrderDto;>
     * @author wliduo[i@dolyw.com]
     * @date 2019-11-20 18:03:33
     */
    public List<StockOrderDto> findPageInfo(StockOrderDto stockOrderDto);
}