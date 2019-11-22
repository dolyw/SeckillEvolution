/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.seckill;

import com.example.dto.custom.StockDto;

/**
 * 统一接口
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
public interface ISeckillService {

    /**
     * 检查库存
     *
     * @param id
     * @return com.example.dto.custom.StockDto
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/20 20:22
     */
    StockDto checkStock(Integer id) throws Exception;

    /**
     * 扣库存
     *
     * @param stockDto
     * @return java.lang.Integer 操作成功条数
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/20 20:24
     */
    Integer saleStock(StockDto stockDto) throws Exception;

    /**
     * 下订单
     *
     * @param stockDto
     * @return java.lang.Integer 操作成功条数
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/20 20:26
     */
    Integer createOrder(StockDto stockDto);

}