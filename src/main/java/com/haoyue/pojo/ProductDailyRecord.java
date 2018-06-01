package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/11/15.
 * 当日目前商品访客数 浏览量  功能暂存
 * 对比昨日数据变化
 *
 */

@Entity
@Table(name = "product_daily_record")
public class ProductDailyRecord {

    @Id
    @GeneratedValue
    private Integer id;

    private String openId;
    private String sellerId;
    private int pid;
    private String pname;
    private int views;
    private int visitors;
    private double rate_view;//浏览量变化率
    private double rate_visitor;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    @Temporal(TemporalType.DATE)
    @Column(updatable=false,nullable = true)
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

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getVisitors() {
        return visitors;
    }

    public void setVisitors(int visitors) {
        this.visitors = visitors;
    }

    public double getRate_view() {
        return rate_view;
    }

    public void setRate_view(double rate_view) {
        this.rate_view = rate_view;
    }

    public double getRate_visitor() {
        return rate_visitor;
    }

    public void setRate_visitor(double rate_visitor) {
        this.rate_visitor = rate_visitor;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
