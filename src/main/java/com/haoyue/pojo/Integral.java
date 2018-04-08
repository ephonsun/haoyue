package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Lijia on 2018/4/8.
 * 积分
 */
@Entity
@Table(name = "integral")
public class Integral {

    @Id
    @GeneratedValue
    private Integer id;

    private String sellerId;

    private String typename;//签到获得积分 0 交易获得积分 1
    private String days;//连续签到天数
    private double expense;//交易金额
    private String scrolls;//分数
    private boolean active=true;//是否开启活动
    private String comments;//备注

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;//创建日期


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

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public String getScrolls() {
        return scrolls;
    }

    public void setScrolls(String scrolls) {
        this.scrolls = scrolls;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
