/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.dao;

import com.example.dto.custom.StockDto;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

/**
 * StockDao
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
@Repository
public interface StockDao extends Mapper<StockDto> {

    /**
     * 列表
     * @param stockDto
     * @return java.util.List<com.example.dto.custom.StockDto;>
     * @author wliduo[i@dolyw.com]
     * @date 2019-11-20 18:03:33
     */
    public List<StockDto> findPageInfo(StockDto stockDto);

}