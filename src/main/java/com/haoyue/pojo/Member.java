package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/10/31.
 * 会员
 */
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue
    private Integer id;

    private String openId;
    private String sellerId;
    private String code;//会员编号
    private String discount;//折扣
    private String wxname;
    private String leavel;//普通会员 高级会员 至尊会员  lev1 lev2 lev3
    private String total_consume;//最低消费
    private String comment1;//领取会员卡下的备注
    private String comment2;
    private String comment3;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期


    public String getComment1() {
        return comment1;
    }

    public void setComment1(String comment1) {
        this.comment1 = comment1;
    }

    public String getComment2() {
        return comment2;
    }

    public void setComment2(String comment2) {
        this.comment2 = comment2;
    }

    public String getComment3() {
        return comment3;
    }

    public void setComment3(String comment3) {
        this.comment3 = comment3;
    }

    public String getTotal_consume() {
        return total_consume;
    }

    public void setTotal_consume(String total_consume) {
        this.total_consume = total_consume;
    }

    public String getLeavel() {
        return leavel;
    }

    public void setLeavel(String leavel) {
        this.leavel = leavel;
    }

    public String getWxname() {
        return wxname;
    }

    public void setWxname(String wxname) {
        this.wxname = wxname;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
