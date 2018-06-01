package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Lijia on 2018/4/10.
 * 买家的每一笔积分记录
 * 禾才小程序客户通过每天签到或者付款获得积分，付款时候可以抵现
 */
@Entity
@Table(name = "integral_record")
public class IntegralRecord {

    @Id
    @GeneratedValue
    private Integer id;

    private String openId;
    private String sellerId;
    private String type;//0 获取积分 1 消费积分
    private int scrolls;//积分的数量
    private String froms;//来源 签到/消费


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;//创建日期

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getScrolls() {
        return scrolls;
    }

    public void setScrolls(int scrolls) {
        this.scrolls = scrolls;
    }

    public String getFroms() {
        return froms;
    }

    public void setFroms(String froms) {
        this.froms = froms;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
