/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.dto.custom;

import java.io.Serializable;
import javax.persistence.Table;
import com.example.dto.domain.StockOrderDtoBase;

/**
 * StockOrderDto
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
@Table(name = "t_seckill_stock_order")
public class StockOrderDto extends StockOrderDtoBase implements Serializable {

    private static final long serialVersionUID = StockOrderDto.class.getName().hashCode();

}