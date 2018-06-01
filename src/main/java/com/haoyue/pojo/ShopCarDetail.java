package com.haoyue.pojo;

import javax.persistence.*;

/**
 * Created by LiJia on 2017/8/25.
 * 购物车详情
 */
@Entity
@Table(name = "shopcardetail")
public class ShopCarDetail {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private ProdutsType produtsType;

    private Integer amount;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProdutsType getProdutsType() {
        return produtsType;
    }

    public void setProdutsType(ProdutsType produtsType) {
        this.produtsType = produtsType;
    }
}
