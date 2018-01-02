package com.haoyue.fq.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/12/22.
 * 收入
 */
@Entity(name = "fq_income")
public class Income {

    @Id
    @GeneratedValue
    private Integer id;

    private String type;//来源
    private double money;
    private String comment;
    private String openId;
    private String babyJoin;//宝宝认可

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;//创建日期

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getBabyJoin() {
        return babyJoin;
    }

    public void setBabyJoin(String babyJoin) {
        this.babyJoin = babyJoin;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
