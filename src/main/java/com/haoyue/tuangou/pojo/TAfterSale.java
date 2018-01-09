package com.haoyue.tuangou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 *  Created by LiJia on 2018/1/8.
 *  售后服务
 */

@Entity
@Table(name = "t_after_sale")
public class TAfterSale {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    private TOrders order;

    @OneToOne
    private TuanOrders  tuanOrders;

    private String openId;
    private String saleId;
    private String formId;

    @Lob
    @Column(columnDefinition="TEXT")
    private String message;

    @Lob
    @Column(columnDefinition="TEXT")
    private String pics;

    private String isAgree;//是否同意 0 等待卖家处理 1 同意 2 不同意

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;//创建日期


    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

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

    public TOrders getOrder() {
        return order;
    }

    public void setOrder(TOrders order) {
        this.order = order;
    }

    public TuanOrders getTuanOrders() {
        return tuanOrders;
    }

    public void setTuanOrders(TuanOrders tuanOrders) {
        this.tuanOrders = tuanOrders;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(String isAgree) {
        this.isAgree = isAgree;
    }
}
