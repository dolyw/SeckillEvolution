/*
 * PDMS wliduo https://github.com/dolyw
 * Created By dolyw.com
 * Date By (2019-11-20 18:03:33)
 */
package com.example.dto.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * StockOrderDtoBase
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
public class StockOrderDtoBase implements Serializable {

    private static final long serialVersionUID = StockOrderDtoBase.class.getName().hashCode();

    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 库存ID */
    private Integer stockId;

    /** 商品名称 */
    private String name;

    /** 创建时间 */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    /**
     * 获取属性id的值
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * 设置属性id的值
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取属性库存ID的值
     */
    public Integer getStockId() {
        return this.stockId;
    }

    /**
     * 设置属性库存ID的值
     */
    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    /**
     * 获取属性商品名称的值
     */
    public String getName() {
        return this.name;
    }

    /**
     * 设置属性商品名称的值
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取属性创建时间的值
     */
    public Date getCreateTime() {
        return this.createTime;
    }

    /**
     * 设置属性创建时间的值
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}