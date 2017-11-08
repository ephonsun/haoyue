package com.haoyue.tuangou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/11/7.
 * 快递模板详细
 */
@Entity
@Table(name = "t_deliver_template")
public class TDeliverTemplate {

    @Id
    @GeneratedValue
    private Integer id;

    private String destinationAddress;
    private double price;
    private String count;
    private double morePrice;
    private String moreCount;
    private String saleId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public double getMorePrice() {
        return morePrice;
    }

    public void setMorePrice(double morePrice) {
        this.morePrice = morePrice;
    }

    public String getMoreCount() {
        return moreCount;
    }

    public void setMoreCount(String moreCount) {
        this.moreCount = moreCount;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
