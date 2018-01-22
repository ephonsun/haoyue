package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/22.
 * 退货、退款中卖家和买家留言，协商详情展示
 */

@Entity
@Table(name = "message")
public class Message  {

    @Id
    @GeneratedValue
    private Integer id;

    private int after_sale_id;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;//创建日期

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAfter_sale_id() {
        return after_sale_id;
    }

    public void setAfter_sale_id(int after_sale_id) {
        this.after_sale_id = after_sale_id;
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
