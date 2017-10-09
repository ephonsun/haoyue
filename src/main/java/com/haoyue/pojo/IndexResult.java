package com.haoyue.pojo;

/**
 * Created by LiJia on 2017/8/25.
 */
public class IndexResult{

    private Products productses;
    private Double turnover;//交易额
    private Integer buyers;//买家数量
    private Integer visitors;//访客数
    private Integer views;//浏览量

    public Products getProductses() {
        return productses;
    }

    public void setProductses(Products productses) {
        this.productses = productses;
    }

    public Double getTurnover() {
        return turnover;
    }

    public void setTurnover(Double turnover) {
        this.turnover = turnover;
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
}
