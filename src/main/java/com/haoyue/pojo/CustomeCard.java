package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Lijia on 2018/4/17.
 * 自定义优惠券 正在使用的优惠券
 */
@Entity
@Table(name = "customecard")
public class CustomeCard {

    @Id
    @GeneratedValue
    private Integer id;

    private String sellerId;
    private String openId;
    private String cardName;//优惠券名称
    private int totalNums;//总发放量
    private int gotNums;//已领取量
    private int gotUsed;//已经使用量
    private String type;//优惠形式 金额 0 折扣 1
    private double typeValue;//优惠额度 1元 0.9折
    private String picSmall;//小图标
    private double requireMent;//使用要求 满 0-n 元可使用
    private int maxGets;//单人最多可领取X张
    private String formId;//用于模板消息
    private boolean remind=true;//到期4天前提醒
    private boolean active=true;
    private boolean used=false;//是否使用
    private boolean share=false;//是否可以分享
    private String whereuse;//可使用商品 0 全部商品 1 指定商品 2 仅原价购买商品
    private String comments;//备注说明
    private String phone;//客服电话
    private String pid;//客户领取的优惠券对应商家设置的优惠券的ID，方便统计领取数量
    private String expiretype;//有效期 0 固定 1 从领取当日开始计算  2 从领取次日开始计算
    private int expiredays;//有效期几日
    private boolean hasremind=false;//是否发过模板消息


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date beginDate;//生效日期
    private String beginDateStr;//生效日期

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date expireDate;//过期日期
    private String expireDateStr;//过期日期


    public boolean getHasremind() {
        return hasremind;
    }

    public void setHasremind(boolean hasremind) {
        this.hasremind = hasremind;
    }

    public int getGotUsed() {
        return gotUsed;
    }

    public void setGotUsed(int gotUsed) {
        this.gotUsed = gotUsed;
    }

    public String getExpiretype() {
        return expiretype;
    }

    public void setExpiretype(String expiretype) {
        this.expiretype = expiretype;
    }

    public int getExpiredays() {
        return expiredays;
    }

    public void setExpiredays(int expiredays) {
        this.expiredays = expiredays;
    }

    public int getGotNums() {
        return gotNums;
    }

    public void setGotNums(int gotNums) {
        this.gotNums = gotNums;
    }

    public boolean getUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getBeginDateStr() {
        return beginDateStr;
    }

    public void setBeginDateStr(String beginDateStr) {
        this.beginDateStr = beginDateStr;
    }

    public String getExpireDateStr() {
        return expireDateStr;
    }

    public void setExpireDateStr(String expireDateStr) {
        this.expireDateStr = expireDateStr;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getTotalNums() {
        return totalNums;
    }

    public void setTotalNums(int totalNums) {
        this.totalNums = totalNums;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(double typeValue) {
        this.typeValue = typeValue;
    }

    public String getPicSmall() {
        return picSmall;
    }

    public void setPicSmall(String picSmall) {
        this.picSmall = picSmall;
    }

    public double getRequireMent() {
        return requireMent;
    }

    public void setRequireMent(double requireMent) {
        this.requireMent = requireMent;
    }

    public int getMaxGets() {
        return maxGets;
    }

    public void setMaxGets(int maxGets) {
        this.maxGets = maxGets;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public boolean getRemind() {
        return remind;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public String getWhereuse() {
        return whereuse;
    }

    public void setWhereuse(String whereuse) {
        this.whereuse = whereuse;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
