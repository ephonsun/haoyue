package com.haoyue.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Lijia on 2018/3/13.
 * 会员分组 用于把所有会员分类
 */
@Entity
@Table(name = "member_group")
public class MemberGroup {

    @Id
    @GeneratedValue
    private Integer id;

    private String content;
    private String sellerId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
