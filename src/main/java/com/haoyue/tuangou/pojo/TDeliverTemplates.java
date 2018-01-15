package com.haoyue.tuangou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/7.
 * 快递模板
 */
@Entity
@Table(name = "t_deliver_templates")
public class TDeliverTemplates {

    @Id
    @GeneratedValue
    private Integer id;
    private String dname;
    private String sendAddress;
    private String priceType;
    private String deliverType;
    private double price;
    private String count;
    private String moreCount;
    private double morePrice;
    private String saleId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<TDeliverTemplate> templates;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(String deliverType) {
        this.deliverType = deliverType;
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

    public String getMoreCount() {
        return moreCount;
    }

    public void setMoreCount(String moreCount) {
        this.moreCount = moreCount;
    }

    public double getMorePrice() {
        return morePrice;
    }

    public void setMorePrice(double morePrice) {
        this.morePrice = morePrice;
    }

    public List<TDeliverTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<TDeliverTemplate> templates) {
        this.templates = templates;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
