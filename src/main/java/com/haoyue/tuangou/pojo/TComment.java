package com.haoyue.tuangou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/11/14.
 * 买家评论
 */
@Entity
@Table(name = "t_comment")
public class TComment {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    private TProducts tProducts;
    @OneToOne
    private TProductsTypes tProductsTypes;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String message;
    private String wxname;//嘴角轻扬30
    private String cutwxname;//嘴**
    private String wxpic;
    private String openId;
    private String saleId;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String pics;//评论图片
    private String replay;//卖家回复
    private String level;//评论等级 1 好评 2 中评 3 差评
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期


    public String getCutwxname() {
        return cutwxname;
    }

    public void setCutwxname(String cutwxname) {
        this.cutwxname = cutwxname;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getReplay() {
        return replay;
    }

    public void setReplay(String replay) {
        this.replay = replay;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
