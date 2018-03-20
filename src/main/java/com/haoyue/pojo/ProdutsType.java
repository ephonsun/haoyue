package com.haoyue.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by LiJia on 2017/8/23.
 * 存放 宝贝分类 颜色 尺码 库存
 * 有 sellerId 无 productId 卖家个人的宝贝分类库
 * 无 sellerId 有 productId 存放指定商品的 颜色 尺码 库存 已卖出量 现价 原价
 */
@Entity
@Table(name = "protypes")
public class ProdutsType {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer productId;

    private Integer sellerId;
    private String color;
    private String size;
    private Integer amount;//库存
    private Double priceNew;//现价
    private Double priceOld;//原价
    private boolean Active=true;
    private boolean isDiscount=false;//是否打折
    private Double discountPrice;//折扣价
    private Double secondKillPrice;//秒杀价
    private String pic;//每个分类对应一个图片

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Double getSecondKillPrice() {
        return secondKillPrice;
    }

    public void setSecondKillPrice(Double secondKillPrice) {
        this.secondKillPrice = secondKillPrice;
    }

    public boolean getIsDiscount() {
        return isDiscount;
    }

    public void setISDiscount(boolean discount) {
        isDiscount = discount;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public boolean getActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public Double getPriceNew() {
        return priceNew;
    }

    public void setPriceNew(Double priceNew) {
        this.priceNew = priceNew;
    }

    public Double getPriceOld() {
        return priceOld;
    }

    public void setPriceOld(Double priceOld) {
        this.priceOld = priceOld;
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
