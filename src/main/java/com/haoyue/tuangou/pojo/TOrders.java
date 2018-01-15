package com.haoyue.tuangou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/11/9.
 * 普通的订单
 */
@Entity
@Table(name = "t_orders")
public class TOrders {

    @Id
    @GeneratedValue
    private Integer id;
    private String code;

    @OneToOne
    private TProducts tProducts;
    @OneToOne
    private TProductsTypes tProductsTypes;
    private int amount;
    @OneToOne
    private TDeliver tDeliver;
    private double productPrice;//下订单时候商品价格
    private double totalPrice;
    private double deliverPrice;
    private String openId;
    private String saleId;
    private String wxname;
    private String wxpic;
    private String state;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date payDate;//付款日期
    private boolean showbuy=true;
    private boolean showsale=true;
    private boolean iscomment=false;
    private boolean isdelay=false;//是否延迟收货
    private boolean isapplyreturn=false;
    private boolean ispayback=false;
    @OneToOne
    private TComment comment;
    private String leavemsg;//买家留言
    private String leavemsg2;//卖家备注
    private String formId;
    private String out_trade_no;


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

    public boolean getIsapplyreturn() {
        return isapplyreturn;
    }

    public void setIsapplyreturn(boolean isapplyreturn) {
        this.isapplyreturn = isapplyreturn;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getLeavemsg() {
        return leavemsg;
    }

    public void setLeavemsg(String leavemsg) {
        this.leavemsg = leavemsg;
    }

    public String getLeavemsg2() {
        return leavemsg2;
    }

    public void setLeavemsg2(String leavemsg2) {
        this.leavemsg2 = leavemsg2;
    }

    public boolean getIsdelay() {
        return isdelay;
    }

    public void setIsdelay(boolean isdelay) {
        this.isdelay = isdelay;
    }

    public TComment getComment() {
        return comment;
    }

    public void setComment(TComment comment) {
        this.comment = comment;
    }

    public boolean getIsComment() {
        return iscomment;
    }

    public void setIscomment(boolean iscomment) {
        this.iscomment = iscomment;
    }

    public double getDeliverPrice() {
        return deliverPrice;
    }

    public void setDeliverPrice(double deliverPrice) {
        this.deliverPrice = deliverPrice;
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

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public TDeliver gettDeliver() {
        return tDeliver;
    }

    public void settDeliver(TDeliver tDeliver) {
        this.tDeliver = tDeliver;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
