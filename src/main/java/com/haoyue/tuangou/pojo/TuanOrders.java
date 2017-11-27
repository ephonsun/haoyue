package com.haoyue.tuangou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/11/9.
 */
@Entity
@Table(name = "t_tuanorders")
public class TuanOrders {

    @Id
    @GeneratedValue
    private Integer id;

    private String groupCode;//房间编号
    private String code;
    private String openId;
    private String saleId;
    private String owner;//房主
    private String ownerpic;
    private int joinNum;
    private int startNum;//开团人数
    private String wxname;
    private String wxpic;
    private String state;
    private int amount;

    @OneToOne
    private TProducts tProducts;
    @OneToOne
    private TProductsTypes tProductsTypes;
    @OneToOne
    private TDeliver tDeliver;

    private double productPrice;
    private double deliverPrice;
    private double totalPrice;
    private int hours;//团购时间
    private boolean isowner=false;//是否房主
    private boolean isover=false;
    private boolean showbuy=true;
    private boolean showsale=true;
    private boolean iscomment=false;
    private boolean ispayback=false;//是否退款

    @OneToOne
    private TComment comment;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date startDate;//开始日期

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date endDate;//结束日期

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    private String out_trade_no;//后期退款时候的凭证

    public boolean getIspayback() {
        return ispayback;
    }

    public void setIspayback(boolean ispayback) {
        this.ispayback = ispayback;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public boolean getIscomment() {
        return iscomment;
    }

    public void setIscomment(boolean iscomment) {
        this.iscomment = iscomment;
    }

    public TComment getComment() {
        return comment;
    }

    public void setComment(TComment comment) {
        this.comment = comment;
    }

    public boolean getShowbuy() {
        return showbuy;
    }

    public void setShowbuy(boolean showbuy) {
        this.showbuy = showbuy;
    }

    public boolean getShowsale() {
        return showsale;
    }

    public void setShowsale(boolean showsale) {
        this.showsale = showsale;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getOwnerpic() {
        return ownerpic;
    }

    public void setOwnerpic(String ownerpic) {
        this.ownerpic = ownerpic;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(int joinNum) {
        this.joinNum = joinNum;
    }

    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public TProducts gettProducts() {
        return tProducts;
    }

    public void settProducts(TProducts tProducts) {
        this.tProducts = tProducts;
    }

    public TProductsTypes gettProductsTypes() {
        return tProductsTypes;
    }

    public void settProductsTypes(TProductsTypes tProductsTypes) {
        this.tProductsTypes = tProductsTypes;
    }

    public TDeliver gettDeliver() {
        return tDeliver;
    }

    public void settDeliver(TDeliver tDeliver) {
        this.tDeliver = tDeliver;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getDeliverPrice() {
        return deliverPrice;
    }

    public void setDeliverPrice(double deliverPrice) {
        this.deliverPrice = deliverPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean getIsowner() {
        return isowner;
    }

    public void setIsowner(boolean isowner) {
        this.isowner = isowner;
    }

    public boolean getIsover() {
        return isover;
    }

    public void setIsover(boolean isover) {
        this.isover = isover;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
