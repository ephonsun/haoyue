package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/9/25.
 * 简易版 商品评论 每个人都可以评论
 */
@Entity
@Table(name = "basiccomments")
public class BasicComments {

    @Id
    @GeneratedValue
    private Integer id;

    private String comments;
    private String wxname;
    private String wxphoto;
    private String proId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getWxname() {
        return wxname;
    }

    public void setWxname(String wxname) {
        this.wxname = wxname;
    }

    public String getWxphoto() {
        return wxphoto;
    }

    public void setWxphoto(String wxphoto) {
        this.wxphoto = wxphoto;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
