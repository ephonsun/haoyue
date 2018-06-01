package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Lijia on 2018/5/17.
 * 文章的用户评论，类似于百度贴吧，多级评论
 */

@Entity
@Table(name = "article_comment")
public class ArticleComment {

    @Id
    @GeneratedValue
    private Integer id;

    private String openId;
    private String wxname;
    private String wxpic;
    private Integer articleId;
    private boolean active=true;
    private String comments;//250字以内

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期

    private Integer pid;
    @OneToMany(cascade = CascadeType.ALL)
    private List<ArticleComment> articleCommentChilds;


    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public List<ArticleComment> getArticleCommentChilds() {
        return articleCommentChilds;
    }

    public void setArticleCommentChilds(List<ArticleComment> articleCommentChilds) {
        this.articleCommentChilds = articleCommentChilds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getWxpic() {
        return wxpic;
    }

    public void setWxpic(String wxpic) {
        this.wxpic = wxpic;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
