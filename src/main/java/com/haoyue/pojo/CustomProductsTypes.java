package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/12/8.
 * 自定义商品分类
 */

@Entity
@Table(name = "customer_pro_types")
public class CustomProductsTypes {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String sellerId;

    private String pid;

    @Lob
    @Column(columnDefinition="TEXT")
    private String  pics;//一级分类对应海报图
    private String keywords;//一级分类对应关键字
    private String maintitle;//主标题
    private String smalltitle;//副标题


    @OneToMany(cascade = CascadeType.ALL)
    private List<CustomProductsTypes> childs;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;//创建日期

    private boolean active=true;


    public String getMaintitle() {
        return maintitle;
    }

    public void setMaintitle(String maintitle) {
        this.maintitle = maintitle;
    }

    public String getSmalltitle() {
        return smalltitle;
    }

    public void setSmalltitle(String smalltitle) {
        this.smalltitle = smalltitle;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<CustomProductsTypes> getChilds() {
        return childs;
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setChilds(List<CustomProductsTypes> childs) {
        this.childs = childs;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
