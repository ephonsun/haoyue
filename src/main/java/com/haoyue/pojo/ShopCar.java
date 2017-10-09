package com.haoyue.pojo;

import javax.persistence.*;
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
