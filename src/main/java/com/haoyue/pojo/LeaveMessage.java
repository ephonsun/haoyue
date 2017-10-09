package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/9/30.
 * 官网网站留言
 */
@Entity
@Table(name = "leave_message")
public class LeaveMessage  {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String phone;
    private String email;
    private String address;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String message;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
