package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/9/14.
 *
 * 访客数记录，同一个用户每天访问多次，只记录一次访问记录，隔天清空
 *
 */
@Entity
@Table(name = "visitors")
public class Visitors {

    @GeneratedValue
    @Id
    private Integer id;
    private Integer sellerId;
    private Integer productId;
    private String openId;
    private int num;//浏览次数
    private String wxname;
    private String wxpic;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;//创建日期

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
