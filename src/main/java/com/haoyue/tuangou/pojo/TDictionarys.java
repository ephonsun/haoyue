package com.haoyue.tuangou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/11/10.
 * 每日数据
 */
@Entity
@Table(name = "t_dictionarys")
public class TDictionarys  {

    @Id
    @GeneratedValue
    private Integer id;

    private String saleId;
    private int views;
    private int visitors;
    private int turnover;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    @Temporal(TemporalType.DATE)
    @Column(updatable=false)
    private Date createDate;//创建日期


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
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

    public int getTurnover() {
        return turnover;
    }

    public void setTurnover(int turnover) {
        this.turnover = turnover;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
