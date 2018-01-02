package com.haoyue.fq.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/12/22.
 * 用户
 */
@Entity(name = "fq_user")
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    private String wxname;
    private String wxpic;
    private String openId;
    private double expenses;//预算支出
    private double real_expenses;//实际支出
    private double rate;//预算支出比

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

    public String getWxname() {
        return wxname;
    }

    public void setWxname(String wxname) {
        this.wxname = wxname;
    }

    public String getWxpic() {
        return wxpic;
    }

    public void setWxpic(String wxpic) {
        this.wxpic = wxpic;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public double getReal_expenses() {
        return real_expenses;
    }

    public void setReal_expenses(double real_expenses) {
        this.real_expenses = real_expenses;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
