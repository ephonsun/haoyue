package com.haoyue.tuangou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/11/2.
 * 卖家
 */
@Entity
@Table(name = "t_usersale")
public class TUserSale {

    @Id
    @GeneratedValue
    private Integer id;
    private String appId;
    private String name;
    private String pass;
    private String email;
    private String phone;
    private double uploadFile;
    private double maxFile;
    private String lunbo;
    private String lunbo_products;//轮播图对应商品编号
    private String onlineCode;
    private String authority;
    private String identification;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期


    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getLunbo_products() {
        return lunbo_products;
    }

    public void setLunbo_products(String lunbo_products) {
        this.lunbo_products = lunbo_products;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(double uploadFile) {
        this.uploadFile = uploadFile;
    }

    public double getMaxFile() {
        return maxFile;
    }

    public void setMaxFile(double maxFile) {
        this.maxFile = maxFile;
    }

    public String getLunbo() {
        return lunbo;
    }

    public void setLunbo(String lunbo) {
        this.lunbo = lunbo;
    }

    public String getOnlineCode() {
        return onlineCode;
    }

    public void setOnlineCode(String onlineCode) {
        this.onlineCode = onlineCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
