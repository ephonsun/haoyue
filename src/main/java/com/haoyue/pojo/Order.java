package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/8/23.
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true, length = 40)
    private String orderCode;//订单号

    @OneToOne
    private Deliver deliver;//快递

    private Integer customerId;//买家Id

    private int amount;//数量

    @ManyToOne
    private Address address;//收货地址

    private Integer sellerId;//卖家Id
    private String sellerName;//卖家名

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "order_product", joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")})
    private List<Products> products;//商品

    @ManyToMany
    @JoinTable(name = "order_protype", joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")})
    private List<ProdutsType> produtsTypes;//商品分类

    private Double price;//订单成立时商品价格

    private Double oldPrice;//商品原价

    @OneToOne
    private Comment comment;//评价

    private String leaveMessage;//买家留言

    private String leaveMessage_seller;//卖家留言

    private String state;//订单状态

    private String payType;//支付类型
    private String invoiceType;//发票类型
    private Double totalPrice;//总计

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;//创建日期

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date payDate;//支付日期

    private boolean active = true;
    private boolean isApplyReturn = false;//是否申请退货
    private boolean isLuckDraw=false;//是否是抽奖订单
    private boolean isLuckDrawEnd=false;//抽奖是否结束
    private String wxname;
    private String luckcode;//抽奖号码
    private boolean isLuck=false;//是否中奖
    private boolean iscomment=false;


    public boolean getIscomment() {
        return iscomment;
    }

    public void setIscomment(boolean iscomment) {
        this.iscomment = iscomment;
    }

    public boolean getIsLuck() {
        return isLuck;
    }

    public void setIsLuck(boolean luck) {
        isLuck = luck;
    }

    public boolean getIsLuckDrawEnd() {
        return isLuckDrawEnd;
    }

    public void setIsLuckDrawEnd(boolean luckDrawEnd) {
        isLuckDrawEnd = luckDrawEnd;
    }

    public String getLuckcode() {
        return luckcode;
    }

    public void setLuckcode(String luckcode) {
        this.luckcode = luckcode;
    }

    public boolean getIsLuckDraw() {
        return isLuckDraw;
    }

    public void setIsLuckDraw(boolean luckDraw) {
        isLuckDraw = luckDraw;
    }

    public String getWxname() {
        return wxname;
    }

    public void setWxname(String wxname) {
        this.wxname = wxname;
    }

    public String getLeaveMessage_seller() {
        return leaveMessage_seller;
    }

    public void setLeaveMessage_seller(String leaveMessage_seller) {
        this.leaveMessage_seller = leaveMessage_seller;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public boolean getIsApplyReturn() {
        return isApplyReturn;
    }

    public void setIsApplyReturn(boolean applyReturn) {
        isApplyReturn = applyReturn;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Deliver getDeliver() {
        return deliver;
    }

    public void setDeliver(Deliver deliver) {
        this.deliver = deliver;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }


    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<ProdutsType> getProdutsTypes() {
        return produtsTypes;
    }

    public void setProdutsTypes(List<ProdutsType> produtsTypes) {
        this.produtsTypes = produtsTypes;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
