package com.haoyue.pojo;

import javax.persistence.*;

/**
 * Created by LiJia on 2017/8/24.  物流
 * 有 sellerId 是物流模板
 * 无 sellerId  是具体的发货物流
 */
@Entity
@Table(name = "deliver")
public class Deliver {

    @Id
    @GeneratedValue
    private Integer id;

    private String sellerId;
    private String destination;//目的地
    private int account;//单件数
    private int more_account;//增加件数
    private Double more_price;//增加邮费

    @Column(unique = true,length = 50)
    private String dcode;//快递号
    @Column(length = 20)
    private String dname;//快递名
    private String dename;//快递名 英文
    private Double price;//运费


    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public int getMore_account() {
        return more_account;
    }

    public void setMore_account(int more_account) {
        this.more_account = more_account;
    }

    public Double getMore_price() {
        return more_price;
    }

    public void setMore_price(Double more_price) {
        this.more_price = more_price;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDename() {
        return dename;
    }

    public void setDename(String dename) {
        this.dename = dename;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }
}
