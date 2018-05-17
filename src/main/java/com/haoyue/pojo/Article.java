package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Lijia on 2018/5/17.
 * 文章
 */

@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue
    private Integer id;


    private String sellerId;
    private String author;//作者
    private String titile;//标题
    private String remark;//摘要
    private String indexpic;
    private int views;//阅读数
    private int thumsup;//点赞
    private boolean active=true;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String pics;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;//内容 <br> 分割内容


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期


    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitile() {
        return titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIndexpic() {
        return indexpic;
    }

    public void setIndexpic(String indexpic) {
        this.indexpic = indexpic;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getThumsup() {
        return thumsup;
    }

    public void setThumsup(int thumsup) {
        this.thumsup = thumsup;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
