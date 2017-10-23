package com.haoyue.pojo;

import javax.persistence.*;

/**
 * Created by LiJia on 2017/9/20.
 *      售后服务
 */

@Entity
@Table(name = "after_sale")
public class AfterSale {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    private Order order;

    private String openId;
    private String sellerId;

    @Lob
    @Column(columnDefinition="TEXT")
    private String message;

    private String isAgree="null";//是否同意 null 等待卖家处理 true 同意 false 不同意

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
