package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/23.
 */
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue
    private Integer id;

    private String level;//好评，中评，差评
    private String message;//评论内容
    @Lob
    @Column(columnDefinition="TEXT")
    private String images;//评论晒图
    private Integer orderId;
    // 在和数据库表字段映射时候该字段除外
    @Transient
    private Order order;
    private String  openId;
    private String wxname;
    private String cutwxname;
    private String wxpic;
    private Integer sellerId;//卖家ID
    private String reversion;//回复
    private String pid;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getCutwxname() {
        return cutwxname;
    }

    public void setCutwxname(String cutwxname) {
        this.cutwxname = cutwxname;
    }

    public String getWxpic() {
        return wxpic;
    }

    public void setWxpic(String wxpic) {
        this.wxpic = wxpic;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getReversion() {
        return reversion;
    }

    public void setReversion(String reversion) {
        this.reversion = reversion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getWxname() {
        return wxname;
    }

    public void setWxname(String wxname) {
        this.wxname = wxname;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
