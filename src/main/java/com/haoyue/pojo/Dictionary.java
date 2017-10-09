package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/8/24.
 * 卖家主页 全部 浏览量 交易额 访客数 买家数量
 */
@Entity
@Table(name = "dictionarys")
public class Dictionary {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer sellerId;
    private Integer productId;//有无商品Id判断是全部还是交易额，浏览量，访客数。。。
    private Integer buyers;//买家数-修改-订单数
    private Integer visitors;//访客数
    private Integer views;//浏览量
    @Column(name = "turnover", columnDefinition="double(10,2)")
    private Double turnover;//交易数

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    @Temporal(TemporalType.DATE)
    @Column(updatable=false,nullable = true)
    private Date createDate;//创建日期


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBuyers() {
        return buyers;
    }

    public void setBuyers(Integer buyers) {
        this.buyers = buyers;
    }

    public Integer getVisitors() {
        return visitors;
    }

    public void setVisitors(Integer visitors) {
        this.visitors = visitors;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Double getTurnover() {
        return turnover;
    }

    public void setTurnover(Double turnover) {
        this.turnover = turnover;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
