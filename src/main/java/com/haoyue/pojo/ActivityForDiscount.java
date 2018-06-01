package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Lijia on 2018/4/23.
 *
 * 折扣活动
 * 新建折扣活动，商品绑定折扣活动后，就会更新商品的折扣价，商品折扣价默认为0
 *
 */
@Entity
@Table(name = "activity_discount")
public class ActivityForDiscount {

    @Id
    @GeneratedValue
    private Integer id;

    private String sellerId;
    private String activityName;//活动名称
    private String activitylabel;//活动标签
    private boolean active=true;
    private int orderNums;//订单数量
    private double totalPrice;//订单总金额
    private int customerNums;//付款人数


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromDate;//开始日期

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;//结束日期


    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivitylabel() {
        return activitylabel;
    }

    public void setActivitylabel(String activitylabel) {
        this.activitylabel = activitylabel;
    }

    public int getOrderNums() {
        return orderNums;
    }

    public void setOrderNums(int orderNums) {
        this.orderNums = orderNums;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCustomerNums() {
        return customerNums;
    }

    public void setCustomerNums(int customerNums) {
        this.customerNums = customerNums;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
