package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/8/25.
 * 购物车
 */
@Entity
@Table(name = "shopcar")
public class ShopCar {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer customerId;
    private String  wxname;
    private Integer sellerId;
    private String  formId;
    private String  formId2;
    private String  openId;

    @ManyToMany
    private List<Products> productses;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ShopCarDetail> shopCarDetails;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;//创建日期

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date endDate;//formid失效日期

    private boolean active=true;//formid是否失效

    public String getFormId2() {
        return formId2;
    }

    public void setFormId2(String formId2) {
        this.formId2 = formId2;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
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

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
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

    public List<Products> getProductses() {
        return productses;
    }

    public void setProductses(List<Products> productses) {
        this.productses = productses;
    }

    public List<ShopCarDetail> getShopCarDetails() {
        return shopCarDetails;
    }

    public void setShopCarDetails(List<ShopCarDetail> shopCarDetails) {
        this.shopCarDetails = shopCarDetails;
    }
}
