/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.service;


/**
 * ISeckillEvolutionService
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
public interface ISeckillEvolutionService {

    /**
     * 传统方式的创建订单(并发会出现错误)
     *
     * @param id
     * @return java.lang.Integer
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/20 20:58
     */
    Integer createWrongOrder(Integer id);

}