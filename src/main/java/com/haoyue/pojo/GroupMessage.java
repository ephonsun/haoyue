package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/9/7.
 * 群里有事--通知
 */
@Entity
@Table(name = "groupMessage")
public class GroupMessage {

    @Id
    @GeneratedValue
    private Integer id;

    private String title;
    @Lob
    @Column(columnDefinition="TEXT")
    private String message;

    private String releaser;//发布者
    @Lob
    @Column(columnDefinition="TEXT")
    private String wxName;
    @Lob
    @Column(columnDefinition="TEXT")
    private String pics;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    @Temporal(TemporalType.DATE)
    private Date createDate;

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReleaser() {
        return releaser;
    }

    public void setReleaser(String releaser) {
        this.releaser = releaser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
