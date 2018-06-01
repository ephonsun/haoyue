package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/11/3.
 * 抽奖
 * 卖家设置的抽奖规则，可通过在商品详情页里设置绑定抽奖
 */
@Entity
@Table(name = "luckdraw")
public class LuckDraw {

    @Id
    @GeneratedValue
    private Integer id;
    private String sellerId;
    private int allNumber;// 所有抽奖号码 从 1 开始 例如 1000 就是 1-1000
    private String lackNumber;//中奖号码 100-200-300-400
    private int joinNumber;//参与人数

    @Lob
    @Column(columnDefinition = "TEXT")
    private String joiners;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期


    public String getJoiners() {
        return joiners;
    }

    public void setJoiners(String joiners) {
        this.joiners = joiners;
    }

    public int getJoinNumber() {
        return joinNumber;
    }

    public void setJoinNumber(int joinNumber) {
        this.joinNumber = joinNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public int getAllNumber() {
        return allNumber;
    }

    public void setAllNumber(int allNumber) {
        this.allNumber = allNumber;
    }

    public String getLackNumber() {
        return lackNumber;
    }

    public void setLackNumber(String lackNumber) {
        this.lackNumber = lackNumber;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
