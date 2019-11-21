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
 * StockDtoBase
 * @author wliduo[i@dolyw.com]
 * @date 2019-11-20 18:03:33
 */
public class StockDtoBase implements Serializable {

    private static final long serialVersionUID = StockDtoBase.class.getName().hashCode();

    /** 库存ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 名称 */
    private String name;

    /** 库存 */
    private Integer count;

    /** 已售 */
    private Integer sale;

    /** 乐观锁，版本号 */
    private Integer version;

    /** 创建时间 */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 获取属性库存ID的值
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * 设置属性库存ID的值
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取属性名称的值
     */
    public String getName() {
        return this.name;
    }

    /**
     * 设置属性名称的值
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取属性库存的值
     */
    public Integer getCount() {
        return this.count;
    }

    /**
     * 设置属性库存的值
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 获取属性已售的值
     */
    public Integer getSale() {
        return this.sale;
    }

    /**
     * 设置属性已售的值
     */
    public void setSale(Integer sale) {
        this.sale = sale;
    }

    /**
     * 获取属性乐观锁，版本号的值
     */
    public Integer getVersion() {
        return this.version;
    }

    /**
     * 设置属性乐观锁，版本号的值
     */
    public void setVersion(Integer version) {
        this.version = version;
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

    /**
     * 获取属性更新时间的值
     */
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**
     * 设置属性更新时间的值
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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