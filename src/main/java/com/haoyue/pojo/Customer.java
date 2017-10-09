package com.haoyue.pojo;

import javax.persistence.*;
import java.util.List;

/**
 * Created by LiJia on 2017/8/21.
 */
@Entity
@Table(name = "Customer", uniqueConstraints={@UniqueConstraint(columnNames={"openId","sellerId"})})
public class Customer {

    @GeneratedValue
    @Id
    private Integer id;

    private String phone;
    private String openId;//小程序用户唯一标识
    private String sellerId;//appid

    @OneToMany
    private List<Address> addressList;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
