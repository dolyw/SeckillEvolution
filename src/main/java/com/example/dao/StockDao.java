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

    /**
     * 乐观锁更新扣减库存
     *
     * @param stockDto
     * @return int
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/22 14:14
     */
    @Update("UPDATE t_seckill_stock SET count = count - 1, sale = sale + 1, version = version + 1 " +
            "WHERE id = #{id, jdbcType = INTEGER} AND version = #{version, jdbcType = INTEGER} " +
            "")
    int updateByOptimisticLock(StockDto stockDto);

}