package com.example.constant;

/**
 * 常量
 *
 * @author wliduo[i@dolyw.com]
 * @date 2018/9/3 16:03
 */
public interface Constant {

    /**
     * redis-OK
     */
    String OK = "OK";

    /**
     * redis过期时间，以秒为单位，一分钟
     */
    int EXPIRE_MINUTE = 60;

    /**
     * redis过期时间，以秒为单位，一小时
     */
    int EXPIRE_HOUR = 60 * 60;

    /**
     * redis过期时间，以秒为单位，一天
     */
    int EXPIRE_DAY = 60 * 60 * 24;

    /**
     * redis-key-示例前缀-example
     */
    String PREFIX_EXAMPLE = "example:";

    /**
     * 商品名称
     */
    String ITEM_STOCK_NAME = "OnePlus 7 Pro";

    /**
     * redis-key-前缀-count-库存
     */
    String PREFIX_COUNT = "stock:count:";

    /**
     * redis-key-前缀-sale-已售
     */
    String PREFIX_SALE = "stock:sale:";

    /**
     * redis-key-前缀-version-乐观锁版本
     */
    String PREFIX_VERSION = "stock:version:";

}
