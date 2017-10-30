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
    private Integer sellerId;

    @ManyToMany
    private List<Products> productses;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ShopCarDetail> shopCarDetails;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;//创建日期

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
